//
//  tbnCaptureWebView.m
//  Capture
//
//  Created by Sacha Best on 4/19/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnCaptureWebView.h"

@implementation tbnCaptureWebView

-(void)drawBuildings:(NSString *)bbox {
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
        [self backgroundDraw:bbox];
    });
}
-(void)backgroundDraw:(NSString *)bbox {
    
    ownerIDs = [[NSMutableArray alloc] init];
    buildingIDs = [[NSMutableArray alloc] init];
    
    // --------------- Data Structures ---------------- //
    // key = way id, value = array of node ids
    polygons = [[NSArray alloc] init];
    // key = node id, value = array of latitude, longitude
    points = [[NSArray alloc] init];
    
    NSArray *bounds = [bbox componentsSeparatedByString:@","]; // w s e n
    tbnXMLParser *parser = [[tbnXMLParser alloc] initWithWebView:self];
    [parser sendXMLRequest:bounds];

    // XML Parser will now call recieveXMLData in this class when it is done.
}
-(void) outerPolygonLoop {
    NSString *o_id = [NSString alloc];
    for (int i = 0; i < [polygons count]; i ++) {
        NSArray *polygon = polygons[i];
        if ([ownerIDs count] > 0) {
            if (ownerIDs[i]) {
                o_id = ownerIDs[i];
            } else o_id = @"";
        } else {
            o_id = @"";
        }
        pointData = [self iterateThroughPolygons:o_id number:i polygon:polygon];
    }
    ownerIDs = [[NSArray alloc] init];
}
-(NSString *) iterateThroughPolygons:(NSString *)o_id number:(int)i polygon:(NSArray *)polygon {
    for( int j = 0; j < [polygons count]; j ++ ) {
        NSArray *lat_lon = points[polygons[j]];
        if(lat_lon) {
            pointData = [[[pointData stringByAppendingString:lat_lon[0]] stringByAppendingString:@","] stringByAppendingString:lat_lon[1]];
        }
        if(j < [polygon count] - 1) {
            [pointData stringByAppendingString:@";"];
        }
    }
    if ([pointData compare:@""] != 0) {
        [self loadURL:buildingIDs data:pointData id:o_id number:i];
        pointData = @"";
    }
    return pointData;
}
-(NSString *) loadURL:(NSArray *)build_ids data:(NSString *)point_data id:(NSString *)o_id number:(int)i {
    NSMutableString *javascript = [[NSMutableString alloc] initWithString:@"drawPolygonFromPoints(\""];
    [javascript appendString:point_data];
    [javascript appendString:@"\",\""];
    [javascript appendString:build_ids[i]];
    [javascript appendString:@"\",\""];
    [javascript appendString:currentID];
    [javascript appendString:@"\",\""];
    [javascript appendString:o_id];
    [javascript appendString:@"\");"];
    return [self stringByEvaluatingJavaScriptFromString:javascript];
}
-(void)recieveXMLData:(NSDictionary *)buildings withPoints:(NSDictionary *)drawPoints {
    pointData = @"";
    buildingIDs = [buildings allKeys];
    ownerIDs = [tbnParseManager makeArrayOfOwners:[tbnParseManager getBuildingsOwnersIDs:buildingIDs]];
    polygons = [buildings allValues];
    points = [drawPoints copy];
    [self outerPolygonLoop];
}
@end
