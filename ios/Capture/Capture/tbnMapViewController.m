//
//  tbnFirstViewController.m
//  Capture
//
//  Created by Sacha Best on 4/14/14.
//  Copyright (c) 2014 The Best Network. All rights reserved.
//

#import "tbnMapViewController.h"
#import "tbnXMLParserTest.h"

@interface tbnMapViewController ()

@end

@implementation tbnMapViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationItem.titleView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"Logo"]];
    if ([tbnParseManager isLoggedIn]) {
        [self loadMap];
    }
    [self.view addSubview:[tbnToolbarView create]];
}
- (void)webViewDidFinishLoad:(UIWebView *)webView {
}
- (void)loadMap {
    NSURL *mapURL = [NSURL URLWithString:kWebMapURL];
    NSURLRequest *map = [NSURLRequest requestWithURL:mapURL];
    [[NSURLCache sharedURLCache] removeAllCachedResponses]; // clear cache
    [_webView loadRequest:map];
    [_webView connectToJavascript];
    _webView.delegate = self;
}
- (void)viewDidAppear:(BOOL)animated {
    if (![tbnParseManager isLoggedIn]) {
        [self showLoginWindow];
    }
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
- (void)showTooltop:(PFObject *)capturePoint x:(float)x y:(float)y {
    
}
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    return YES;
}
- (void)webViewDidStartLoad:(UIWebView *)webView {}
- (void)logInViewController:(PFLogInViewController *)logInController didLogInUser:(PFUser *)user {
    [self dismissViewControllerAnimated:YES completion:^{
        [self loadMap];
    }];
}
@end
