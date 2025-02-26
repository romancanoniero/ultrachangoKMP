import SwiftUI
//import Firebase
//import FirebaseAuth
//import GoogleSignIn



class AppDelegate: NSObject, UIApplicationDelegate {

  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

 // Token de depuracion =      57C9ACF5-542F-4C87-AB43-939EEC43FB05
 //   FirebaseConfig().initialize()
 //   FirebaseApp.configure()
   // let providerFactory = AppCheckDebugProviderFactory()
   // AppCheck.setAppCheckProviderFactory(providerFactory)

   //   FirebaseApp.configure()
    // AppInitializer.shared.onApplicationStart()

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
*/
          // Handle other custom URL types.

          // If not handled by this app, return false.
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
