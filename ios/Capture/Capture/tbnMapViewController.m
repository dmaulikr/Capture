//
//  tbnFirstViewController.m
//  Capture
//
//  Created by Sacha Best on 4/14/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnMapViewController.h"

@interface tbnMapViewController ()

@end

@implementation tbnMapViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    if (![tbnParseManager isLoggedIn]) {
        [self showLoginWindow];
    }
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

- (void) showLoginWindow {
    PFLogInViewController *login = [[PFLogInViewController alloc] init];
    login.delegate = self;
    [self presentViewController:login animated:YES completion:nil];
}

@end
