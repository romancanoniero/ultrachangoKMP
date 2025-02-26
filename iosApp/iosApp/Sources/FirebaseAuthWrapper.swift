
import Foundation
/*
import FirebaseAuth

//@objc
@objcMembers
public class FirebaseAuthWrapper: NSObject {
    static let shared = FirebaseAuthWrapper()

    @objc func signIn(email: String, password: String, completion: @escaping (String?, String?) -> Void) {
        Auth.auth().signIn(withEmail: email, password: password) { result, error in
            if let error = error {
                completion(nil, error.localizedDescription)
            } else {
                completion(result?.user.uid, nil)
            }
        }
    }

    @objc func signUp(email: String, password: String, completion: @escaping (String?, String?) -> Void) {
        Auth.auth().createUser(withEmail: email, password: password) { result, error in
            if let error = error {
                completion(nil, error.localizedDescription)
            } else {
                completion(result?.user.uid, nil)
            }
        }
    }

    @objc func signOut() {
        do {
            try Auth.auth().signOut()
        } catch {
            print("Error signing out: \(error.localizedDescription)")
        }
    }

    @objc func getCurrentUser() -> String? {
        return Auth.auth().currentUser?.uid
    }
}
*/