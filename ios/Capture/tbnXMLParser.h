//
//  tbnXMLParser.h
//  Capture
//
//  Created by Sacha Best on 4/20/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface tbnXMLParser : NSObject <NSURLConnectionDataDelegate, NSXMLParserDelegate> {
    NSXMLParser *internalParser;
    NSString *currentWayID;
    NSMutableArray *currentWayData;
    BOOL shouldAddWay;
    BOOL justFoundWay;
}

@property NSArray *buildingIDs;
@property NSMutableDictionary *nodes;
@property NSMutableArray *ways;

- (void) sendXMLRequest:(NSArray *)bounds;
- (void) parse;
- (void)manualParse:(NSString *)data;

@end
