//
//  tbnParseManager.h
//  Capture
//
//  Created by Sacha Best on 4/14/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Parse/Parse.h>

#define __PARSE__

#define kParseCapturePointDefense @"defense"
#define kParseCapturePointOwner @"currentOwner"
#define kParseCapturePointClass @"CapturePoint"
#define kParseCapturePointNodes @"nodes"
#define kParseCapturePointID @"pointID"


@interface tbnParseManager : NSObject

typedef enum LoginResult {
    SUCCESS, USER_TAKEN, EMAIL_TAKEN, INVALID
} LoginResult;

+ (BOOL) isLoggedIn;
+ (PFUser *) getCurrentUser;
+ (void) capturePoint:(PFObject *)point withNewArmy:(int)army withTarget:(id)target selector:(SEL)selector;
+ (void) createPoint:(NSArray *)nodes atPointID:(NSString *)pointID withTarget:(id)target selector:(SEL)selector;
+ (PFObject *) getPointByID:(NSString *)pointID;
+ (NSArray *) getBuildingsByOwner:(PFUser *)owner;
+ (NSArray *) getBuildingsOwnersIDs:(NSArray *)buildings;
+ (NSArray *) makeArrayOfOwners:(NSArray *)objects;

+ (void) showLoginWindow;

@end
