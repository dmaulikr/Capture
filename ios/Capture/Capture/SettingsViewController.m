//
//  SettingsViewController.m
//  Capture
//
//  Created by Sacha Best on 3/4/14.
//  Copyright (c) 2014 Sacha Best. All rights reserved.
//

#import "SettingsViewController.h"

@implementation SettingsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationItem.titleView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"Logo"]];
    username = [[UITextField alloc] initWithFrame:CGRectMake(self.view.frame.size.width / 2, (44-17) / 2.0, (self.view.frame.size.width) / 2,  18)];
    username.delegate = self;
    email = [[UITextField alloc] initWithFrame:CGRectMake(self.view.frame.size.width / 2, (44-17) / 2.0, (self.view.frame.size.width) / 2,  18)];
    email.delegate = self;
    username.text = [tbnParseManager getCurrentUser].username;
    email.text = [tbnParseManager getCurrentUser][@"email"];
    _userPhoto.file = [tbnParseManager getUserPhotoFetched];
    [_userPhoto loadInBackground:^(UIImage *image, NSError *error) {
        _userPhoto.frame = CGRectMake(_userPhoto.frame.origin.x, _userPhoto.frame.origin.y, 200, 200);
        [_userPhoto.layer setCornerRadius:100];
        [_userPhoto.layer setBorderWidth:0];
        [_userPhoto.layer setMasksToBounds:YES];
    }];
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = nil;
    NSString *identifier = nil;
    switch (indexPath.row) {
        case 0:
            identifier = @"username";
            break;
        case 1:
            identifier = @"email";
            break;
        case 2:
        default:
            break;
    }
    cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }
    
    switch (indexPath.row) {
        case 0:
            cell.textLabel.text = @"Username";
            [cell addSubview:username];

            break;
        case 1:
            cell.textLabel.text = @"Email";
            [cell addSubview:email];

            break;
        case 2:
        default:
            break;
    }
    cell.textLabel.font = [UIFont boldSystemFontOfSize:17];
    return cell;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 2;
}
- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section{
    return @"User Information";
}
- (void)textFieldDidEndEditing:(UITextField *)textField {
    if (textField == username) {
        [PFUser currentUser].username = textField.text;
    } else if (textField == email) {
     //   [PFUser currentUser][@"phone"] = [FriendCell trimNumber:textField.text];
    }
    [textField resignFirstResponder];
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [self textFieldDidEndEditing:textField];
    return YES;
}
- (IBAction)close:(id)sender {
    //temp;
}
@end
