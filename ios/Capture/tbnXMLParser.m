//
//  tbnXMLParser.m
//  Capture
//
//  Created by Sacha Best on 4/20/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnXMLParser.h"

@implementation tbnXMLParser

- (id)init {
    _nodes = [[NSMutableDictionary alloc] init];
    _ways = [[NSMutableArray alloc] init];
    return self;
}
- (void)sendXMLRequest:(NSArray *)bounds {
    NSMutableDictionary *nameValuePairs = [[NSMutableDictionary alloc] initWithCapacity:2];
    NSString *data = [NSString stringWithFormat:@"%s%s%s%@%s%@%s%@%s%@%s%s", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><osm-script timeout=\"900\" element-limit=\"1073741824\">", "<query type=\"node\">", "<bbox-query s=\"", bounds[1], "\" w=\"", bounds[0], "\" n=\"", bounds[3], "\" e=\"", bounds[2], "\"/>", "</query><union><item /><recurse type=\"node-way\"/></union><print/></osm-script>"];
    [nameValuePairs setObject:data forKey:@"form-data"];
    NSData *postData = [data dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
    NSString *postLength = [NSString stringWithFormat:@"%lu",(unsigned long)[postData length]];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:kOverpassAPI]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    NSURLConnection *conn = [[NSURLConnection alloc]initWithRequest:request delegate:self];
    if (conn) {
        NSLog(@"Connection Successful");
    } else {
        [NSException raise:@"Connection could not be made on XML Request" format:nil];
    }
}
- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData*)data {
    internalParser = [[NSXMLParser alloc] initWithData:data];
    [self parse];
}
- (void)manualParse:(NSString *)data {
    internalParser = [[NSXMLParser alloc] initWithData:[data dataUsingEncoding:NSUTF8StringEncoding]];
    [self parse];
}
- (void)parse {
    BOOL result = [internalParser parse];
    if (!result) {
        [NSException raise:@"Parse of XML was unsuccessful" format:nil];
    }
}
- (void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qualifiedName attributes:(NSDictionary *)attributeDict {
    if ([elementName isEqualToString:@"node"]) {
        // Store the node object, all data is included
        [_nodes setObject:attributeDict forKey:[attributeDict objectForKey:@"id"]];
    } else if ([elementName isEqualToString:@"way"]) {
        // Store the way object template and fill in later
        shouldAddWay = false;
        currentWayID = [attributeDict objectForKey:@"id"];
        currentWayData = [[NSMutableArray alloc] init];
        [_nodes setObject:[[NSMutableDictionary alloc] init] forKey:currentWayID];
    } else if ([elementName isEqualToString:@"nd"]) {
        [currentWayData addObject:[attributeDict objectForKey:@"ref"]];
    }
}
- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName {
    if ([elementName isEqualToString:@"way"]) {
        if (shouldAddWay) {
            [_ways addObject:currentWayData];
            shouldAddWay = false;
            currentWayData = NULL;
            currentWayID = NULL;
        }
    }
}
@end
