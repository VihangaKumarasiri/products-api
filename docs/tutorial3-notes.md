# Tutorial 3 — Lab Notes

## Activity 0 — Predict the output

Setup: `products` is a `List<Product>` holding 2 items, `fixed` is a `Product[2]`, `byId` is a `Map<Long, Product>` holding key `1L` only.

| # | Statement | Compiles? | What happens, or why not |
|---|-----------|-----------|--------------------------|
| 1 | `products.add(new Product(3L, "Mug", 8.50))` | Yes | Adds the product. The list now holds 3 elements. |
| 2 | `products.add("Mug")` | No | The list is `List<Product>`, so `add` only accepts a `Product`. A `String` is rejected. |
| 3 | `String s = products.get(0)` | No | `get` returns a `Product`, and a `Product` cannot be assigned to a `String`. |
| 4 | `Product p = products.get(5)` | Yes | The types match, but the list only has 2 items. Throws `IndexOutOfBoundsException` at runtime. |
| 5 | `int n = products.size()` | Yes | Returns 2. |
| 6 | `fixed.add(new Product(3L, "Mug", 8.50))` | No | An array has no `add` method. Arrays are assigned by index (`fixed[0] = ...`). |
| 7 | `Product q = byId.get(99L)` | Yes | Returns `null`. A missing key is not an error. |
| 8 | `System.out.println(q.getName())` | Yes | `q` is `null`, so this throws `NullPointerException` at runtime. |

Rows 4 and 8 compile but fail at runtime. The compiler checks types only. It cannot know how many items a list holds or whether a map has a key.

## Activity 4 — Full sweep (tasks 14 and 15)

Results below were checked with an automated MockMvc run against the running application context, not by hand in Insomnia.

| Call | Status | Body |
|------|--------|------|
| `POST /products` `{"name":"Laptop","price":899.99}` | 200 | `{"id":1,"name":"Laptop","price":899.99}` |
| `POST /products` `{"name":"Mouse","price":24.99}` | 200 | `{"id":2,"name":"Mouse","price":24.99}` |
| `GET /products` | 200 | Both products |
| `GET /products/1` | 200 | The Laptop |
| `DELETE /products/1` | 200 | empty |
| `GET /products` | 200 | Only the Mouse (id 2) |
| `GET /products/search?name=Lap` | 200 | Only products whose name contains "Lap" (e.g. "Gaming Laptop") |
| `GET /products/search?name=zzz` | 200 | `[]` |

### Task 15 — `GET /products/99`

Status `200` with an empty body.

This is the wrong answer. `200 OK` means "here is what you asked for", but there was nothing to give. A missing resource should return `404 Not Found` with a message saying what was missing. The cause is `.orElse(null)` in the controller, which turns "no product" into an empty `200` response. `DELETE /products/99` has the same flaw and reports success for a product that never existed. Lecture 6 fixes both with custom exceptions and `@ControllerAdvice`.

## Other checks

- Ids stay correct past 127: `GET /products/130` finds its product because the service compares with `.equals()`, not `==`.
- `GET /products/search` without `name` returns `400`.
- `POST /products` with an array body returns `400`, and with no JSON content type returns `415`.
