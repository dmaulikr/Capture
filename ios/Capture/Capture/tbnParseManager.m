//
//  tbnParseManager.m
//  Capture
//
//  Created by Sacha Best on 4/14/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnParseManager.h"

@implementation tbnParseManager

+ (void) capturePoint:(PFObject *)point withNewArmy:(int)army withTarget:(id)target selector:(SEL)selector {
    NSString *oldOwner = point[kParseCapturePointOwner];
    [point setObject:[NSNumber numberWithInt:army] forKey:kParseCapturePointDefense];
    [point setObject:[PFUser currentUser].objectId forKey:kParseCapturePointOwner];
    if (oldOwner) {
        [tbnParseManager sendPush:oldOwner];
    }
    if (!target || !selector) {
        [point saveInBackground];
    } else {
        [point saveInBackgroundWithTarget:target selector:selector];
    }
}
+ (void)capturePointByNodeID:(NSString *)pointID withNewArmy:(int)army withTarget:(id)target selector:(SEL)selector {
    PFQuery *ptQuery = [PFQuery queryWithClassName:kParseCapturePointClass];
    [ptQuery whereKey:kParseCapturePointID equalTo:pointID];
    [ptQuery findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        [tbnParseManager capturePoint:objects[0] withNewArmy:army withTarget:target selector:selector];
    }];
}
+ (void) createPoint:(int)army atPointID:(NSString *)pointID withTarget:(id)target selector:(SEL)selector {
    PFObject *newPoint = [[PFObject alloc] initWithClassName:kParseCapturePointClass];
    [newPoint setObject:0 forKey:kParseCapturePointDefense];
    [newPoint setObject:[NSNumber numberWithInt:army] forKey:kParseCapturePointDefense];
    [newPoint setObject:pointID forKey:kParseCapturePointID];
    if (!target || !selector) {
        [newPoint saveInBackground];
    } else {
        [newPoint saveInBackgroundWithTarget:target selector:selector];
    }
}
+ (PFObject *) getPointByID:(NSString *)pointID {
    PFQuery *forID = [PFQuery queryWithClassName:kParseCapturePointClass];
    [forID whereKey:kParseCapturePointID equalTo:pointID];
    NSArray *results = [forID findObjects];
    if (results.count > 0) {
        return results[0];
    } else {
        return NULL;
    }
}
+ (NSArray *) getBuildingsByOwner:(PFUser *)owner {
    PFQuery *search = [[PFQuery alloc] initWithClassName:kParseCapturePointClass];
    [search whereKey:kParseCapturePointOwner equalTo:owner];
    return [search findObjects];
}
+ (NSDictionary *) getBuildingsOwnersIDs:(NSArray *)buildings {
    PFQuery *search = [[PFQuery alloc] initWithClassName:kParseCapturePointClass];
    [search whereKey:kParseCapturePointID containedIn:buildings];
    NSArray *results = [search findObjects];
    NSMutableDictionary *buildingToOwner = [[NSMutableDictionary alloc] initWithCapacity:buildings.count];
    for (int i = 0; i < buildings.count; i++) {
        [buildingToOwner setObject:@"" forKey:buildings[i]];
        for (int j = 0; j < results.count; j++) {
            if ([[results[j] objectForKey:kParseCapturePointID] isEqualToString:buildings[i]]) {
                [buildingToOwner setObject:[results[j] objectForKey:kParseCapturePointOwner] forKey:buildings[i]];
            }
        }
    }
    return [buildingToOwner copy];
}
+ (NSArray *) makeArrayOfOwners:(NSArray *)objects {
    if (!objects)
        return NULL;
    NSMutableArray *results = [[NSMutableArray alloc] initWithCapacity:objects.count];
    for (int i = 0; i < objects.count; i++) {
        if (objects[i]) {
            results[i] = [objects[i] objectForKey:kParseCapturePointOwner];
        } else {
            results[i] = NULL;
        }
    }
    return [results copy];
}
+ (BOOL)isLoggedIn {
    if ([PFUser currentUser]) {
        [[PFUser currentUser] refresh];
        return true;
    }
    return false;
}
+ (PFUser *)getCurrentUser {
    [[PFUser currentUser] fetchIfNeeded];
    return [PFUser currentUser];
}
+ (PFFile *)getUserPhotoFetched {
    PFObject *photo = [tbnParseManager getCurrentUser][@"photo"];
    [photo fetchIfNeeded];
    return photo[@"fullSize"];
}
+ (void)sendPush:(NSString *)userID {
    PFQuery *userToTarget = [PFUser query];
    [userToTarget whereKey:@"objectId" equalTo:userID];
    [userToTarget findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        PFQuery *sendQuery = [PFInstallation query];
        [sendQuery whereKey:@"user" equalTo:objects[0]];
        NSString *msg = [NSString stringWithFormat:@"Your building was captured by %@!", [tbnParseManager getCurrentUser].username];
        [PFPush sendPushMessageToQuery:sendQuery withMessage:msg error:nil];
    }];
}
@end
