//
//  tbnFirstViewController.m
//  Capture
//
//  Created by Sacha Best on 4/14/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnFirstViewController.h"

@interface tbnFirstViewController ()

@end

@implementation tbnFirstViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    // TODO: clear webview cache

    // iOS handles location request natively
    
    NSURL *mapURL = [NSURL URLWithString:kWebMapURL];
    NSURLRequest *map = [NSURLRequest requestWithURL:mapURL];
    [_webView loadRequest:map];
    
    // TODO: Now need to send geolocation information through javascript
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
