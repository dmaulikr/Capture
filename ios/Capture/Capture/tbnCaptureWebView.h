//
//  tbnCaptureWebView.h
//  Capture
//
//  Created by Sacha Best on 4/19/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "tbnXMLParser.h"

@interface tbnCaptureWebView : UIWebView {
    NSString *currentID;
    NSArray *buildingIDs;
    NSArray *ownerIDs;
    NSDictionary *points;
    NSArray *polygons;
    NSString *pointData;
}


-(void)drawBuildings:(NSString *)bbox;
-(void)backgroundDraw:(NSString *)bbox;
-(void)recieveXMLData:(NSDictionary *)buildings withPoints:(NSDictionary *)drawPoints;
-(void) outerPolygonLoop;
-(NSString *) iterateThroughPolygons:(NSString *)o_id number:(int)i polygon:(NSArray *)polygon;
-(NSString *) loadURL:(NSArray *)build_ids data:(NSString *)point_data id:(NSString *)o_id number:(int)i;

@end
