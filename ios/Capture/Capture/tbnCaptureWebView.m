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
        dispatch_async(dispatch_get_main_queue(), ^{
            [self outerPolygonLoop:ownerIDs buildings:buildingIDs polygons:polygons points:points data:pointData];
        });
    });
}
-(void)backgroundDraw:(NSString *)bbox {
    
    ownerIDs = [[NSMutableArray alloc] init];
    buildingIDs = [[NSMutableArray alloc] init];
    
    // --------------- Data Structures ---------------- //
    // key = way id, value = array of node ids
    polygons = [[NSMutableArray alloc] init];
    // key = node id, value = array of latitude, longitude
    points = [[NSMutableDictionary alloc] init];
    
    NSArray *bounds = [bbox componentsSeparatedByString:@","]; // w s e n
    
    // TODO : XML handler
    // create parser
    // String output = xmlHandler.getXMLDataFromBBox(bounds, httpclient, httppost);
    
    // ------------ Parsing XML -------------- //
    // Create a DOM element to parse XML
    /*
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true); // allows access to localName
    DocumentBuilder db = factory.newDocumentBuilder();
    InputSource inStream = new InputSource();
    inStream.setCharacterStream(new StringReader(output));
    Document doc = db.parse(inStream);
    */
    // populate polygons
    //polygons = xmlHandler.getPolygonData(doc);
    
    // populate points
    //points = xmlHandler.getPointData(doc);
    
    //buildIDs = xmlHandler.getBuildIds();
    
    pointData = @"";
    NSMutableArray *b_o = [[NSMutableArray alloc] initWithCapacity:[buildingIDs count]];
    for (int k = 0; k < [buildingIDs count]; k++) {
        b_o[k] = [buildingIDs objectAtIndex:k];
    }
    NSArray *outArray = [tbnParseManager makeArrayOfOwners:[tbnParseManager getBuildingsOwnersIDs:b_o]];
    for (int k = 0; k < [outArray count]; k++) {
        [ownerIDs addObject:outArray[k]];
    }
}
-(void) outerPolygonLoop:(NSMutableArray *)owner_ids buildings:(NSMutableArray *)build_ids polygons:(NSMutableArray *)polygons points:(NSDictionary *)points data:(NSString *)point_data {
    NSString *o_id = [NSString alloc];
    for (int i = 0; i < [polygons count]; i ++) {
        NSArray *polygon = polygons[i];
        if ([owner_ids count] > 0) {
            if (owner_ids[i]) {
                o_id = owner_ids[i];
            } else o_id = @"";
        } else {
            o_id = @"";
        }
        point_data = [self iterateThroughPolygons:build_ids points:points data:point_data id:o_id number:i polygon:polygon];
    }
    [owner_ids removeAllObjects];
}
-(NSString *) iterateThroughPolygons:(NSMutableArray *)build_ids points:(NSDictionary *)points data:(NSString *)point_data id:(NSString *)o_id number:(int)i polygon:(NSArray *)polygon {
    for( int j = 0; j < [polygons count]; j ++ ) {
        NSArray *lat_lon = points[polygons[j]];
        if(lat_lon) {
            point_data = [[[point_data stringByAppendingString:lat_lon[0]] stringByAppendingString:@","] stringByAppendingString:lat_lon[1]];
        }
        if(j < [polygon count] - 1) {
            [point_data stringByAppendingString:@";"];
        }
    }
    if ([point_data compare:@""] != 0) {
        [self loadURL:build_ids data:point_data id:o_id number:i];
        point_data = @"";
    }
    return point_data;
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
    [javascript appendString:@"\")"];
    return [self stringByEvaluatingJavaScriptFromString:javascript];
}

@end
