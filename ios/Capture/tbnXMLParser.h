//
//  tbnXMLParser.h
//  Capture
//
//  Created by Sacha Best on 4/20/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "tbnCaptureWebView.h"

@class tbnCaptureWebView;

@interface tbnXMLParser : NSObject <NSXMLParserDelegate> {
    NSXMLParser *internalParser;
    NSString *currentWayID;
    NSMutableArray *currentWayData;
    BOOL shouldAddWay;
    BOOL justFoundWay;
}

@property tbnCaptureWebView *webView;
@property NSMutableDictionary *nodes;
@property NSMutableDictionary *ways;

- (id)initWithWebView:(tbnCaptureWebView *)view;
- (void) sendXMLRequest:(NSArray *)bounds;
- (void) parse;
- (void)manualParse:(NSString *)data;

@end
