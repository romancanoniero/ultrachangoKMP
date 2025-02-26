import AppIntents




struct AddProductIntent: AppIntent {
    static var title: LocalizedStringResource = "Agregar un Producto a la Lista"

    @Parameter(title: "Nombre del Producto")
    var productName: String

    @Parameter(title: "Nombre de la Lista")
    var listName: String

    @MainActor
    func perform() async throws -> some IntentResult {
        // Aquí puedes llamar a la lógica de Compose Multiplatform para agregar el producto.
        print("Agregando \(productName) a la lista \(listName)")

        return .result(dialog: "Agregué \(productName) a la lista \(listName).")
    }
     static var openAppWhenRun: Bool = false
}

@main
struct MyAppIntents: AppIntents {
    static var intents: [AppIntent.Type] {
        [AddProductIntent()]
    }
}
