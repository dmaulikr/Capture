//
//  tbnCaptureWebView.h
//  Capture
//
//  Created by Sacha Best on 4/19/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface tbnCaptureWebView : UIWebView {
    NSString *currentID;
    NSMutableArray *buildingIDs;
    NSMutableArray *ownerIDs;
    NSMutableDictionary *points;
    NSMutableArray *polygons;
    NSString *pointData;
}


-(void)drawBuildings:(NSString *)bbox;
-(void)backgroundDraw:(NSString *)bbox;
-(void) outerPolygonLoop:(NSMutableArray *)owner_ids buildings:(NSMutableArray *)build_ids polygons:(NSMutableArray *)polygons points:(NSDictionary *)points data:(NSString *)point_data;
-(NSString *) iterateThroughPolygons:(NSMutableArray *)build_ids points:(NSDictionary *)points data:(NSString *)point_data id:(NSString *)o_id number:(int)i polygon:(NSArray *)polygon;
-(NSString *) loadURL:(NSArray *)build_ids data:(NSString *)point_data id:(NSString *)o_id number:(int)i;

@end
