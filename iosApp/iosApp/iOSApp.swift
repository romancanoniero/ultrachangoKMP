import SwiftUI
//import FirebaseCore
//import FirebaseAuth
//import GoogleSignIn



class AppDelegate: NSObject, UIApplicationDelegate {

 
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

 // Token de depuracion =      57C9ACF5-542F-4C87-AB43-939EEC43FB05
      
      /*
      UserDefaults.standard.setValue(true, forKey: "FIRDebugEnabled")
      print("Antes de FirebaseApp.configure() - 2")
      FirebaseApp.configure()
      print("Despues de FirebaseApp.configure() - 2")
   */
      
      return true
  }

    func application(
          _ app: UIApplication,
          open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]
        ) -> Bool {

      /*
            var handled: Bool
          handled = GIDSignIn.sharedInstance.handle(url)
          if handled {
            return true
          }
          // Handle other custom URL types.

          // If not handled by this app, return false.
       */
       return false
        }


}

    
    
@main
struct iOSApp: App {

     init() {
        //   FirebaseApp.configure()
        }

    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    var body: some Scene {
        WindowGroup {

       //  ContentView()

        ContentView()
        }
    }
}
