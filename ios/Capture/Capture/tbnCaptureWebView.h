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
    NSDictionary *buildingIDs;
    NSDictionary *ownerIDs;
    NSDictionary *points;
    NSString *pointData;
}


-(void)drawBuildings:(NSString *)bbox;
-(void)backgroundDraw:(NSString *)bbox;
-(void)recieveXMLData:(NSDictionary *)buildings withPoints:(NSDictionary *)drawPoints;
-(void) outerPolygonLoop;
-(NSString *) iterateThroughPolygons:(NSString *)o_id buildingID:(NSString *)b_id polygon:(NSArray *)polygon;
-(NSString *) loadURL:(NSString *)o_id buildingID:(NSString *)b_id point:(NSString *)data current:(NSString *)current;

@end
