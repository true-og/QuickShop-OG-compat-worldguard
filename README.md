# QuickShop-OG WorldGuard Compatibility

GPLv3 compatibility extension for QuickShop-OG and WorldGuard.

The extension adds WorldGuard flags for shop control:

- `quickshop-og-create` controls whether players can create shops in a region.
- `quickshop-og-trade` controls whether players can trade with shops in a region.

It also applies the region's ownership rules to shop permissions and shop limits.

## Requirements

- Java 17
- QuickShop-OG, based on the 5.2.0.7 API
- WorldGuard 7.0.9 or a compatible release

## Build

Run `./gradlew build` from this repository. The resulting plugin JAR is `build/libs/QuickShop-OG-compat-worldguard-5.9.jar`.

## Install

Place the built JAR in the server's `plugins/` directory beside `QuickShop-OG.jar` and the WorldGuard JAR. Restart the server; do not place this JAR in `plugins/QuickShop-OG/`.

## License

GPL-3.0-only. See [LICENSE](LICENSE).
