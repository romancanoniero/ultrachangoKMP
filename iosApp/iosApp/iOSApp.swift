import SwiftUI
import FirebaseCore

import GoogleSignIn

/*

class AppDelegate: NSObject, UIApplicationDelegate {

 
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {



      return true
  }

    func application(
          _ app: UIApplication,
          open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]
        ) -> Bool {

        if GIDSignIn.sharedInstance.handle(url) {
             return true
        }
    // Manejar otras URLs si es necesario
       return false
         
        }


}
*/
    
    
@main
struct iOSApp: App {
     init() {
         FirebaseApp.configure()
     }

  //  @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
