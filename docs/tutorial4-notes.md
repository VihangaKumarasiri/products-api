# Tutorial 4 — Lab Notes

## Activity 0 — Where does each piece live?

Question for each row: "Does every single product need this?"

| # | Member | Which class declares it? | Justification |
|---|--------|--------------------------|---------------|
| 1 | `id : Long` | Product | Every product needs an identity, whether downloaded or shipped. |
| 2 | `name : String` | Product | Every product has a name. |
| 3 | `downloadUrl : String` | DigitalProduct | Only a downloaded product has a URL to download from. |
| 4 | `price : double` | Product | Every product is sold for a price. |
| 5 | `weightKg : double` | PhysicalProduct | Only a shipped item has a weight. |
| 6 | `getName() : String` | Product | It reads `name`, which lives in `Product`, so the getter lives there too. |
| 7 | `fileSizeMB : double` | DigitalProduct | Only a download has a file size. |
| 8 | `dimensions : String` | PhysicalProduct | Only a boxed item has dimensions. |
| 9 | `getPrice() : double` | Product | It reads `price`, which lives in `Product`. |

Five of the nine go in `Product` (rows 1, 2, 4, 6, 9). These are written once instead of twice.

## Activity 1, task 6 — deleting `super(id, name, price)`

In this project, deleting the `super(...)` line does not give a compile error. `Product` has a no-arg constructor, so Java inserts `super()` automatically and the code compiles. The object then has `id = null`, `name = null` and `price = 0.0`, because the parent's constructor never received the values. The "no default constructor available in Product" error only appears when the parent has no no-arg constructor. This is the quiet kind of bug the guide's warning is about.

## Activity 2, task 9 — IS-A

A `PhysicalProduct` IS-A `Product`: it has an id, a name and a price, and a shop can list it wherever it lists any other product. A `ShoppingCart` fails the test. It is not a kind of product. It HAS products inside it, so it should hold a `List<Product>` as a field.

## Activity 3 — One list, two types

Task 11 result of `GET /products` (checked with an automated MockMvc run, not by hand):

```json
[
  {"id":1,"name":"Clean Code (E-book)","price":29.99,"downloadUrl":"/downloads/clean-code.pdf","fileSizeMB":4.2},
  {"id":2,"name":"27-inch Monitor","price":249.99,"weightKg":5.4,"dimensions":"61cm x 41cm x 18cm"}
]
```

Task 12: `id`, `name` and `price` come from `Product`. Neither subclass declares them. Both subclasses inherit `Product`'s public getters (`getId()`, `getName()`, `getPrice()`), and Jackson builds the JSON by calling the getters on each object.

Task 13: `POST /products` with a `downloadUrl` and `fileSizeMB` returns `200` with id 3, and the product appears in `GET /products`. However, the response only contains `id`, `name` and `price`. The controller's `addProduct` takes a plain `Product`, so Jackson builds a `Product` and ignores the two extra fields. The subclass fields are lost on the way in, even though they are shown correctly for the two seeded objects. Making POST keep them would need a change to the controller, which today's guide says not to make.

Discussion prompt: nothing broke in `GET /products` because the list is `List<Product>` and each element IS-A `Product`. Jackson serialises the real object, so each subclass shows its own extra fields.
