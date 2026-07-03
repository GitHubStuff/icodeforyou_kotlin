# CLAUDE.md

Root instructions for this Android/Kotlin workspace. Applies to every project
under this folder. Package-specific rules live in each project's own CLAUDE.md.

## Overview

- Gradle multi-project build (Kotlin DSL). Each subproject is an independent
  Android app or library sharing this root's conventions and tooling.
- Language: Kotlin (JVM target 21). No new Java sources.
- UI: Jetpack Compose. No new XML layouts or Fragments.
- Min SDK 26, target/compile SDK 36.

## Layout

- `settings.gradle.kts` — single source of truth for included projects (`include(...)`).
- `gradle/libs.versions.toml` — version catalog. All dependency versions and
  plugin versions declared here, never inline in module `build.gradle.kts`.
- `build-logic/` — convention plugins shared across modules.
- `<app-or-lib>/` — one directory per subproject.

## Commands

- Build everything: `./gradlew build`
- Single project: `./gradlew :project-name:assembleDebug`
- Unit tests: `./gradlew test`
- Lint (detekt + ktlint): `./gradlew detekt ktlintCheck`
- Coverage report: `./gradlew koverHtmlReport`

## Architecture

- Clean Architecture, three layers per feature: `data`, `domain`, `presentation`.
- `domain` is pure Kotlin — no Android, Compose, or framework imports.
- Presentation is MVI: immutable `State`, sealed `Intent`, unidirectional flow.
  ViewModels expose `StateFlow`; no mutable state leaks to the UI.
- Dependency injection via Hilt. Constructor injection only — no service locators,
  no `@Inject lateinit` on properties where a constructor param works.
- Async via coroutines + Flow. No callbacks, no RxJava, no `runBlocking` in
  production code.

## Conventions

- SOLID by default. Favor small single-responsibility classes and segregated
  interfaces over wide ones.
- Depend on abstractions across layer boundaries, never on concrete `data` types
  from `presentation`.
- Immutability first: `val` over `var`, `data class` with `copy()`, read-only
  collection types in public APIs.
- One public type per file; file name matches the type.
- Errors: model expected failures as sealed `Result`/`Either` types. Reserve
  exceptions for truly exceptional cases.

## Testing

- TDD. Write the failing test first.
- Frameworks: JUnit5 + MockK + Turbine (Flow) + Compose UI test.
- Coverage target: 100% line coverage on `domain` and `data`; enforced by Kover.
- Tests are deterministic — inject dispatchers via a `DispatcherProvider`, never
  reference `Dispatchers.Main`/`IO` directly.

## Always / Never

- ALWAYS add new dependencies through `libs.versions.toml`, then reference the
  catalog alias in the module.
- ALWAYS run `detekt`, `ktlintCheck`, and `test` before considering work done.
- NEVER edit generated files or anything under `build/`.
- NEVER introduce a new dependency without asking — check the catalog first.
- NEVER commit API keys or secrets; use `local.properties` (gitignored).