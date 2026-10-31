# Identidade Coffee Xp

O símbolo reúne um filtro de café de cerâmica e uma xícara com alça. O marcador rosé remete ao diário de degustações. A composição é compacta e dispensa a gota: filtro, abertura de café e xícara precisam continuar reconhecíveis em miniatura.

A alça foi encurtada e a posição foi ajustada para centralizar visualmente o eixo do filtro e da xícara. O gerador verifica tanto o centro das bordas visíveis quanto o alinhamento desse eixo, inclusive nas versões circulares.

As cores vêm do tema existente em `shared/.../theme/Color.kt`:

| Uso | Cor do tema | Hex |
| --- | --- | --- |
| Fundo e espaços negativos | LightPrimary / espresso | `#271310` |
| Cerâmica | LightSurface / papel | `#FAF9F5` |
| Borda e marcador | DarkPrimary / rosé | `#E3BEB8` |

## Fonte e exportações

`tools/GenerateAppIcons.java` contém a geometria vetorial única. Ele gera os SVGs, todos os PNGs Android, os vetores adaptativos e monocromáticos, as imagens de janela Desktop e os arquivos ICO/ICNS. Não editar os derivados manualmente.

```powershell
$iconBuildDir = Join-Path $env:TEMP 'coffee-xp-icon-gen'
New-Item -ItemType Directory -Force -Path $iconBuildDir | Out-Null
javac -d $iconBuildDir tools/GenerateAppIcons.java
java -cp $iconBuildDir GenerateAppIcons .
```

- `coffee-xp-icon.svg`: arte com fundo espresso.
- `coffee-xp-mark.svg`: símbolo com recortes transparentes.
- `icon-preview.png`: tamanhos reais de 16–128 px, formatos de máscara Android e versão temática.
- `android-launcher-detail.png`: captura do APK instalado no launcher do emulador, sem redimensionar o recorte.
- Android: PNGs de 48/72/96/144/192 px, camadas adaptativas em 108 dp e versão temática para API 33+.
- Desktop: PNG principal 1024 px; imagens de janela em 16/20/24/32/40/48/64/128/256 px; ICO com nove resoluções; ICNS com onze representações, incluindo Retina.

Os PNGs usam renderização em quatro vezes o tamanho final e redução bicúbica. Nos tamanhos de 16–24 px, o marcador vira uma faixa simples dentro da xícara e os sulcos ficam mais grossos.

## Recorte Android e verificações

O recurso adaptativo tem fundo opaco, sem cantos incorporados. A geometria do primeiro plano já é exportada em coordenadas de 108 dp, sem transformações de grupo. Cabe no círculo seguro de diâmetro 66 dp, conforme a [documentação Android](https://developer.android.com/develop/ui/compose/system/icon_design_adaptive). A prévia simula a janela central de 72 dp do launcher, e não apenas um PNG com cantos arredondados.

O gerador falha se a geometria sair do círculo seguro, se as entradas do ICO/ICNS não decodificarem ou se houver dimensões incorretas nas imagens de janela. Também verifica a transparência dos cantos dos PNGs circulares.

O APK foi instalado no emulador Android e o launcher foi inspecionado visualmente. A janela Desktop iniciou com os recursos de múltiplas resoluções. APK e MSI foram montados com:

```powershell
.\gradlew.bat --offline --no-configuration-cache :androidApp:assembleDebug :desktopApp:packageMsi
```

Os recursos macOS/Linux foram exportados; os respectivos pacotes precisam ser executados nos seus sistemas. Os recursos de iOS não fazem parte desta alteração.
