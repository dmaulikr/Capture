//
//  tbnCaptureWebView.h
//  Capture
//
//  Created by Sacha Best on 4/19/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "tbnXMLParser.h"
#import "EasyJSWebView.h"

@interface tbnCaptureWebView : EasyJSWebView <UIWebViewDelegate, UIAlertViewDelegate> {
    NSString *currentID;
    NSDictionary *buildingIDs;
    NSDictionary *ownerIDs;
    NSDictionary *points;
    NSString *pointData;
    
    int captureNewArmy;
    NSString *capturePointID;
}


/**
 * Called by XMLParser when data is available
 **/
-(void)recieveXMLData:(NSDictionary *)buildings withPoints:(NSDictionary *)drawPoints;
-(void)backgroundDraw:(NSString *)bbox;

/**
 * Building draw helper methods
 **/
-(void) outerPolygonLoop;
-(NSString *) iterateThroughPolygons:(NSString *)o_id buildingID:(NSString *)b_id polygon:(NSArray *)polygon data:(NSMutableString *)ptLocal;
-(NSString *) loadURL:(NSString *)o_id buildingID:(NSString *)b_id point:(NSMutableString *)data current:(NSString *)current;
- (void)updateMapAfterCapture;

/**
 * Javascript primer
 **/
-(void)connectToJavascript;

/**
 * Javascript invoked methods
 **/
-(void)showBuildings:(NSString *)newBounds;
- (void)showBuildingDialog:(NSString *) ids :(NSString *)closeBy :(NSString *)owner_id :(NSString *)armyS;
- (void) setCurrentIdInJs;


@end

