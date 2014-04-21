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
    NSArray *bounds = [bbox componentsSeparatedByString:@","]; // w s e n
    tbnXMLParser *parser = [[tbnXMLParser alloc] initWithWebView:self];
    [parser sendXMLRequest:bounds];
    // XML Parser will now call recieveXMLData in this class when it is done.
}
-(void) outerPolygonLoop {
    NSString *o_id = [NSString alloc];
    for (NSString *buildingID in ownerIDs) {
        PFUser *owner = [ownerIDs objectForKey:buildingID];
        if (![owner isEqual:@0]) {
            o_id = owner.objectId;
        }
        pointData = [self iterateThroughPolygons:o_id buildingID:buildingID polygon:[buildingIDs objectForKey:buildingID]];
    }
    ownerIDs = [[NSDictionary alloc] init];
}
-(NSString *) iterateThroughPolygons:(NSString *)o_id buildingID:(NSString *)b_id polygon:(NSArray *)polygon {
    for (NSString *buildingID in buildingIDs) {
        NSArray *lat_lon = [buildingIDs objectForKey:buildingID];
        if (lat_lon) {
            pointData = [[[pointData stringByAppendingString:lat_lon[0]] stringByAppendingString:@","] stringByAppendingString:lat_lon[1]];
            pointData = [pointData stringByAppendingString:@";"];
        }
    }
    if (pointData.length > 2)
        pointData = [pointData substringToIndex:pointData.length - 1];
    if ([pointData compare:@""] != 0) {
        // Must draw UI elements and edit WebView on the main thread
        dispatch_async(dispatch_get_main_queue(), ^{
            // Copy to avoid concurrent EXC_BAD_ACCESS b/c concurrent modification from 2 threads
            [self loadURL:o_id buildingID:b_id point:[pointData copy] current:[currentID copy]];
        });
        pointData = @"";
    }
    return pointData;
}
// holds method parameters for same reason as above EXC_BAD_ACCESS
-(NSString *) loadURL:(NSString *)o_id buildingID:(NSString *)b_id point:(NSString *)data current:(NSString *)current {
    NSMutableString *javascript = [[NSMutableString alloc] initWithString:@"drawPolygonFromPoints(\""];
    [javascript appendString:data];
    [javascript appendString:@"\",\""];
    [javascript appendString:b_id];
    [javascript appendString:@"\",\""];
    [javascript appendString:current];
    [javascript appendString:@"\",\""];
    [javascript appendString:o_id];
    [javascript appendString:@"\");"];
    return [self stringByEvaluatingJavaScriptFromString:javascript];
}
-(void)recieveXMLData:(NSDictionary *)buildings withPoints:(NSDictionary *)drawPoints {
    pointData = @"";
    buildingIDs = buildings;
    ownerIDs = [tbnParseManager getBuildingsOwnersIDs:buildingIDs.allKeys];
    currentID = [tbnParseManager getCurrentUser].objectId;
    points = [drawPoints copy];
    [self outerPolygonLoop];
}
@end
