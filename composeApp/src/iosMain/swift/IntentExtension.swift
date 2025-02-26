@main
struct IntentExtension: AppIntentsExtension {
}

struct Shortcuts: AppShortcutsProvider {
    static var appShortcuts: [AppShortcut] {
        AppShortcut(intent: Intent(), phrases: ["Perform intent"])

        AppShortcut(intent: AddProductIntent(), phrases: ["Agrega un producto a la lista"])

    }
}
