//
//  tbnXMLParser.m
//  Capture
//
//  Created by Sacha Best on 4/20/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnXMLParser.h"

@implementation tbnXMLParser

- (id)initWithWebView:(tbnCaptureWebView *)view {
    _nodes = [[NSMutableDictionary alloc] init];
    _ways = [[NSMutableDictionary alloc] init];
    _webView = view;
    return self;
}
- (void)sendXMLRequest:(NSArray *)bounds {
    NSString *data = [NSString stringWithFormat:@"%s%s%s%@%s%@%s%@%s%@%s%s", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><osm-script timeout=\"900\" element-limit=\"1073741824\">", "<query type=\"node\">", "<bbox-query s=\"", bounds[1], "\" w=\"", bounds[0], "\" n=\"", bounds[3], "\" e=\"", bounds[2], "\"/>", "</query><union><item /><recurse type=\"node-way\"/></union><print/></osm-script>"];
    NSData *postData = [data dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
    NSString *postLength = [NSString stringWithFormat:@"%lu",(unsigned long)[postData length]];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:kOverpassAPI]];
    [request setHTTPMethod:@"POST"];\
    [request setValue:@"application/xml; charset=utf-8" forHTTPHeaderField:@"Content-Type"];

    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setHTTPBody:postData];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
        NSURLResponse *temp;
        NSError *error;
        NSData *conn = [NSURLConnection sendSynchronousRequest:request returningResponse:&temp error:&error];
        [self didReceiveData:conn];
    });
}
- (void)didReceiveData:(NSData*)data {
    internalParser = [[NSXMLParser alloc] initWithData:data];
    internalParser.delegate = self;
    [self parse];
}
- (void)manualParse:(NSString *)data {
    internalParser = [[NSXMLParser alloc] initWithData:[data dataUsingEncoding:NSUTF8StringEncoding]];
    internalParser.delegate = self;
    [self parse];
}
- (void)parse {
    BOOL result = [internalParser parse];
    if (!result) {
        [NSException raise:@"Parse of XML was unsuccessful" format:nil];
    }
}
// MY PARSER IS BETTER THAN YOUR PARSER BITCH
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qualifiedName attributes:(NSDictionary *)attributeDict {
    justFoundWay = false;
    if ([elementName isEqualToString:@"node"]) {
        // Store the node object, all data is included
        [_nodes setObject:attributeDict forKey:[attributeDict objectForKey:@"id"]];
    } else if ([elementName isEqualToString:@"way"]) {
        // Store the way object template and fill in later
        shouldAddWay = false;
        justFoundWay = true;
        currentWayID = [attributeDict objectForKey:@"id"];
        currentWayData = [[NSMutableArray alloc] init];
        [_nodes setObject:[[NSMutableDictionary alloc] init] forKey:currentWayID];
    } else if ([elementName isEqualToString:@"nd"]) {
        [currentWayData addObject:[attributeDict objectForKey:@"ref"]];
    } else if ([elementName isEqualToString:@"tag"]) {
        if (currentWayData && [[attributeDict objectForKey:@"k"] isEqualToString:@"building"]) {
            shouldAddWay = true;
        }
    }
}
- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    if ([elementName isEqualToString:@"way"]) {
        if (shouldAddWay && !justFoundWay) {
            [_ways setObject:currentWayData forKey:currentWayID];
            shouldAddWay = false;
            currentWayData = NULL;
            currentWayID = NULL;
        }
    }
}
- (void)parserDidEndDocument:(NSXMLParser *)parser {
    [_webView recieveXMLData:_ways withPoints:_nodes];
    NSLog(@"%@", _ways);
}
@end
