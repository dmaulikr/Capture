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

@interface tbnCaptureWebView : EasyJSWebView <UIWebViewDelegate> {
    NSString *currentID;
    NSDictionary *buildingIDs;
    NSDictionary *ownerIDs;
    NSDictionary *points;
    NSString *pointData;
}


-(void)drawBuildings:(NSString *)bbox;
-(void)backgroundDraw:(NSString *)bbox;
-(void)recieveXMLData:(NSDictionary *)buildings withPoints:(NSDictionary *)drawPoints;
-(void) outerPolygonLoop;
-(NSString *) iterateThroughPolygons:(NSString *)o_id buildingID:(NSString *)b_id polygon:(NSArray *)polygon data:(NSMutableString *)ptLocal;
-(NSString *) loadURL:(NSString *)o_id buildingID:(NSString *)b_id point:(NSMutableString *)data current:(NSString *)current;
-(void)showBuildings:(NSString *)newBounds;
-(void)connectToJavascript;

@end

