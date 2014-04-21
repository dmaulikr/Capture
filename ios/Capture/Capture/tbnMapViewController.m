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
}
- (void)webViewDidFinishLoad:(UIWebView *)webView {
    NSString *jsResult = [webView stringByEvaluatingJavaScriptFromString:@"getBounds();"];
    [_webView drawBuildings:jsResult];
}
- (void)loadMap {
    NSURL *mapURL = [NSURL URLWithString:kWebMapURL];
    NSURLRequest *map = [NSURLRequest requestWithURL:mapURL];
    [[NSURLCache sharedURLCache] removeAllCachedResponses]; // clear cache
    [_webView loadRequest:map];
    _webView.delegate = self;
}
- (void)viewDidAppear:(BOOL)animated {
    //[self.view addSubview:[tbnToolbarView create]];
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
- (void)logInViewController:(PFLogInViewController *)logInController didLogInUser:(PFUser *)user {
    [self dismissViewControllerAnimated:YES completion:^{
        [self loadMap];
    }];
}
@end
