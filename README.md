# Digital Wardrobe Assistant

A Java console application that helps a user manage a digital wardrobe: register clothing items by **type, color, season and occasion**, and prepare outfit requests. Built with a **layered architecture** (UI / Controller / Model / Persistence), **role-based access** and a plain-Java test suite.

Academic group project developed at ISEP (Systems Engineering, Analysis and Modelling of Systems course), following a use-case driven design.

## Features

**Visitor**
- Register a user account (email must be unique, passwords are stored as salted hashes)

**User**
- Add, edit and remove clothing items
- Mark an item as unavailable
- Browse the personal wardrobe (each user only sees their own)
- Request an outfit by occasion and season

**Administrator**
- Define the catalogs the wardrobe is built from: clothing types, colors, occasions, seasons and characteristics
- Catalogs ship with sensible defaults (12 clothing types, 12 colors, 4 seasons, 7 occasions, 8 characteristics) that the administrator can extend at any time

## Architecture

```
Iteration 1/src/main/java/org
├── UI/           console menus, one class per use case
├── Controller/   use-case logic; works on a copy of the domain and only commits when valid
├── Model/        domain: AssistenteDigital, PecaVestuario, GuardaRoupa, Utilizador, catalogs, ...
├── Persistence/  optional file persistence (Java serialization)
└── Utils/        input helpers, validation, password hashing
```

Design choices worth noting:
- **Transactional updates:** each operation runs on a copy of the domain and is published only after it validates and saves, so a failure never leaves half-applied data.
- **Authorization in the domain**, not only in the menus: administrator-only operations are enforced in the model.
- **Batch confirmation:** catalog entries are collected in a draft list and registered all at once after the user confirms.

## Run

Requires **JDK 17 or newer**. No external libraries.

```bash
git clone https://github.com/1251102joao/digital-wardrobe-assistant.git
cd "digital-wardrobe-assistant/Iteration 1"

sh verificar.sh        # compile and run the 113 checks (Windows: verificar.bat)
sh compilar.sh         # build dist/assistente-vestuario.jar (Windows: compilar.bat)
java -jar dist/assistente-vestuario.jar --demo
```

Demo mode loads sample data in memory. Accounts: `admin@demo.pt` / `Admin123!` and `utilizador@demo.pt` / `Demo123!`. In any prompt, type `0` to cancel.

It also opens directly in IntelliJ IDEA through `pom.xml` (see `GUIA_INTELLIJ.txt`).

## Project status

This repository holds **Iteration 1**. Outfit generation is intentionally left pending: the composition rule is still to be defined for the course, so requesting an outfit prepares and validates the choices but does not generate combinations yet.

The user interface and documentation are in European Portuguese.

## Author

**João Pimentel**, Systems Engineering student at ISEP (Porto).
