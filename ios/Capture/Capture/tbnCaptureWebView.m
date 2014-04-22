//
//  tbnCaptureWebView.m
//  Capture
//
//  Created by Sacha Best on 4/19/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnCaptureWebView.h"

@implementation tbnCaptureWebView

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
        if ([owner isMemberOfClass:[PFObject class]]) {
            o_id = owner.objectId;
            NSArray *building = [buildingIDs objectForKey:buildingID];
            NSMutableString *ptData = [[NSMutableString alloc] init];
            [self iterateThroughPolygons:o_id buildingID:buildingID polygon:building data:ptData];
        }
    }
    // clear dictionary for next map-move
    ownerIDs = [[NSDictionary alloc] init];
}
-(NSString *) iterateThroughPolygons:(NSString *)o_id buildingID:(NSString *)b_id polygon:(NSArray *)polygon data:(NSMutableString *)ptLocal {
    for (NSDictionary *nodeData in polygon) {
        if (nodeData) {
            NSString *lat = [nodeData objectForKey:@"lat"];
            NSString *lon = [nodeData objectForKey:@"lon"];
            [ptLocal appendString:lat];
            [ptLocal appendString:@","];
            [ptLocal appendString:lon];
            [ptLocal appendString:@";"];
        }
    }
    if (ptLocal && ptLocal.length > 2) {
        [ptLocal deleteCharactersInRange:NSMakeRange([ptLocal length]-1, 1)];
        // Must draw UI elements and edit WebView on the main thread
        dispatch_async(dispatch_get_main_queue(), ^{
            [self loadURL:o_id buildingID:b_id point:ptLocal current:currentID];
        });
    }
    return pointData;
}
// holds method parameters for same reason as above EXC_BAD_ACCESS
-(NSString *) loadURL:(NSString *)o_id buildingID:(NSString *)b_id point:(NSString *)data current:(NSMutableString *)current {
    NSMutableString *javascript = [[NSMutableString alloc] initWithString:@"drawPolygonFromPoints(\""];
    [javascript appendString:data];
    [javascript appendString:@"\",\""];
    [javascript appendString:b_id];
    [javascript appendString:@"\",\""];
    [javascript appendString:current];
    [javascript appendString:@"\",\""];
    [javascript appendString:o_id];
    [javascript appendString:@"\",0);"];
    NSString *result = [self stringByEvaluatingJavaScriptFromString:javascript];
    NSLog(@"draw");
    NSLog(javascript);
    return result;
}
-(void)recieveXMLData:(NSDictionary *)buildings withPoints:(NSDictionary *)drawPoints {
    pointData = @"";
    buildingIDs = buildings;
    ownerIDs = [tbnParseManager getBuildingsOwnersIDs:buildingIDs.allKeys];
    currentID = [tbnParseManager getCurrentUser].objectId;
    points = [drawPoints copy];
    [self outerPolygonLoop];
}
-(void)showBuildings:(NSString *)newBounds {
    NSLog(@"Javscript invoked");
    [self backgroundDraw:newBounds];
}
-(void)connectToJavascript {
    [self addJavascriptInterfaces:self WithName:@"Android"];
}
@end
