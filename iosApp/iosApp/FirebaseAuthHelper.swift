//
//  FirebaseAuthHelper.swift
//  iosApp
//
//  Created by Roman Canoniero on 15/02/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import Firebase
import FirebaseCore
import FirebaseAuth

@objc public class FirebaseAuthHelper: NSObject {

    @objc public static func getCurrentUser() -> [String: String?]? {
        if let user = Auth.auth().currentUser {
            return ["uid": user.uid, "email": user.email]
        } else {
            return nil
        }
    }

    @objc public static func signUp(email: String, password: String, completion: @escaping (([String: String?]?, Error?) -> Void)) {
        Auth.auth().createUser(withEmail: email, password: password) { (result, error) in
            if let error = error {
                completion(nil, error)
            } else if let user = result?.user {
                completion(["uid": user.uid, "email": user.email], nil)
            } else {
                completion(nil, NSError(domain: "FirebaseAuthHelper", code: 0, userInfo: [NSLocalizedDescriptionKey: "Unknown error"]))
            }
        }
    }

    // Similar functions for signInWithEmailAndPassword, signInWithGoogle, signOut, etc.
    // ...
}
