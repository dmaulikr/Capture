//
//  tbnXMLParserTest.m
//  Capture
//
//  Created by Sacha Best on 4/20/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnXMLParserTest.h"

@implementation tbnXMLParserTest

+ (void)testXML {
    NSArray *bounds = @[ @"-75.19626080989838", @"39.94932571622599", @"-75.19076764583588", @"39.95169032300769" ];
    tbnXMLParser *parser = [[tbnXMLParser alloc] init];
    [parser manualParse:kXMLParserTestValue];
}
@end
