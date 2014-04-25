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
    for (NSString *buildingID in ownerIDs.keyEnumerator) {
        NSString *owner = [ownerIDs objectForKey:buildingID];
//        if (![owner isEqualToString:@""]) { Commented out to allow for all buildings to be drawn
            o_id = owner;
            NSArray *building = [buildingIDs objectForKey:buildingID];
            NSMutableString *ptData = [[NSMutableString alloc] init];
            [self iterateThroughPolygons:o_id buildingID:buildingID polygon:building data:ptData];
//        }
    }
    // clear dictionary for next map-move
    ownerIDs = [[NSDictionary alloc] init];
}
-(NSString *) iterateThroughPolygons:(NSString *)o_id buildingID:(NSString *)b_id polygon:(NSArray *)polygon data:(NSMutableString *)ptLocal {
    for (NSDictionary *nodeData in polygon) {
        if (nodeData) {
            NSString *lat = [nodeData objectForKey:@"lat"];
            NSString *lon = [nodeData objectForKey:@"lon"];
            if (![lat isEqualToString:@""]) {
                [ptLocal appendString:lat];
                [ptLocal appendString:@","];
                [ptLocal appendString:lon];
            }
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
// Javascript Interface
-(void)showBuildings:(NSString *)newBounds {
    NSLog(@"Javscript invoked");
    [self backgroundDraw:newBounds];
}
// Javascript Interface
- (void)showBuildingDialog:(NSString *)ids :(NSString *)closeBy :(NSString *)owner_id :(NSString *)armyS {
    captureNewArmy = [armyS intValue];
    capturePointID = ids;
    captureOldOwnerID = owner_id;
    NSString *message = [NSString stringWithFormat:@"The owner id is %@ with %d army holding the building.", owner_id, captureNewArmy];
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Capture Structure" message:message delegate:self cancelButtonTitle:@"Cancel" otherButtonTitles:@"OK", nil];
    alert.alertViewStyle = UIAlertViewStylePlainTextInput;
    [alert textFieldAtIndex:0].keyboardType = UIKeyboardTypeNumberPad;
    [alert show];
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    switch (buttonIndex) {
        case 0:
            break;
        case 1:
            if (captureOldOwnerID) {
                [tbnParseManager capturePointByNodeID:capturePointID withNewArmy:captureNewArmy withTarget:self selector:@selector(updateMapAfterCapture)];
            } else {
                [tbnParseManager createPoint:captureNewArmy atPointID:capturePointID withTarget:self selector:@selector(updateMapAfterCapture)];
            }
        default:
            break;
    }
}
// Javascript Interface
- (void)updateMapAfterCapture {
    NSString *javascript = [NSString stringWithFormat:@"captureChangeColorAndArmy(\"%@\", \"%d\", \"%@\");", capturePointID, captureNewArmy, [tbnParseManager getCurrentUser].objectId];
    [self stringByEvaluatingJavaScriptFromString:javascript];
}
// Javascript Interface
- (void) setCurrentIdInJs {
    NSString *objectID;
    if ([tbnParseManager isLoggedIn]) {
        objectID = [tbnParseManager getCurrentUser].objectId;
    } else {
        objectID = kParseDefaultUserID;
    }
    NSString *js = [NSString stringWithFormat:@"%@%@%@", @"getCurrentId(\"",  objectID, @"\");"];
    [self stringByEvaluatingJavaScriptFromString:js];
}
-(void)connectToJavascript {
    [self addJavascriptInterfaces:self WithName:@"Android"];
}
@end
