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
    [point setObject:army forKey:kParseCapturePointDefense];
    [point setObject:[PFUser currentUser] forKey:kParseCapturePointOwner];
    if (!target || !selector) {
        [point saveInBackground];
    } else {
        [point saveInBackgroundWithTarget:target selector:selector];
    }
}
+ (void) createPoint:(NSArray *)nodes atPointID:(NSString *)pointID withTarget:(id)target selector:(SEL)selector {
    PFObject *newPoint = [[PFObject alloc] initWithClassName:kParseCapturePointClass];
    [newPoint setObject:0 forKey:kParseCapturePointDefense];
    [newPoint setObject:nodes forKey:kParseCapturePointNodes];
    [newPoint setObject:pointID forKey:kParseCapturePointID];
    if (!target || !selector) {
        [point saveInBackground];
    } else {
        [point saveInBackgroundWithTarget:target selector:selector];
    }
}
+ (PFObject *) getPointByID:(NSString *)pointID {
    PFQuery *forID = [PFQuery queryWithClassName:kParseCapturePointClass];
    [forID whereKey:kParseCapturePointID equalTo:pointID];
    NSArray *results = [forID findObjects];
    if (results.count > 0)
        return results[0];
    } else {
        return NULL;
    }
}
    public static ParseObject[] getBuildingsByOwner(ParseUser user) throws ParseException {
        ParseQuery forUser = ParseQuery.getQuery("CapturePoint");
        forUser.whereEqualTo("owner", user);
        return (ParseObject[]) forUser.find().toArray();
    }
    public static ParseObject[] getBuildingsOwnersIds(String[] buildings) throws ParseException {
        int size = 0;
        size = buildings.length;
        String[] result = new String[size];
        ParseQuery query = ParseQuery.getQuery("CapturePoint");
        query.whereContainedIn("pointID", Arrays.asList(buildings));
        if (query.find().size() > 0) {
            return (ParseObject[]) query.find().toArray(new ParseObject[size]);
        } else return null;
    }
    public static String[] makeArrayOfOwners(ParseObject[] objects) throws java.text.ParseException {
        if (objects == null) return new String[0];
        int size = objects.length;
        String[] result = new String[size];
        String[] objs = new String[size];
        for (int i = 0; i < objs.length; i++) {
            if (objects[i] != null) {
                result[i] = (String) objects[i].get("ownerID").toString();
            } else {
                result[i] = null;
            }
        }
        return result;
    }
}

@end
