//
//  SettingsViewController.h
//  Capture
//
//  Created by Sacha Best on 3/4/14.
//  Copyright (c) 2014 Sacha Best. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SettingsViewController : UIViewController <UITextFieldDelegate, UIGestureRecognizerDelegate> {
    UITextField *username;
    UITextField *email;
}
@property (weak, nonatomic) IBOutlet PFImageView *userPhoto;
@property (weak, nonatomic) IBOutlet UITableView *tableView;

- (IBAction)close:(id)sender;

@end
