---
name: Artisanal Brew Narrative
colors:
  surface: '#121412'
  surface-dim: '#121412'
  surface-bright: '#383a37'
  surface-container-lowest: '#0d0f0d'
  surface-container-low: '#1a1c1a'
  surface-container: '#1e201e'
  surface-container-high: '#282a28'
  surface-container-highest: '#333533'
  on-surface: '#e2e3df'
  on-surface-variant: '#d3c3c0'
  inverse-surface: '#e2e3df'
  inverse-on-surface: '#2f312e'
  outline: '#9c8d8b'
  outline-variant: '#504442'
  surface-tint: '#e3beb8'
  primary: '#e3beb8'
  on-primary: '#422a26'
  primary-container: '#3e2723'
  on-primary-container: '#ae8d87'
  inverse-primary: '#745853'
  secondary: '#ddc1b7'
  on-secondary: '#3e2c26'
  secondary-container: '#56423b'
  on-secondary-container: '#cbafa6'
  tertiary: '#cfc4c0'
  on-tertiary: '#352f2d'
  tertiary-container: '#322c29'
  on-tertiary-container: '#9c938f'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffdad4'
  primary-fixed-dim: '#e3beb8'
  on-primary-fixed: '#2b1613'
  on-primary-fixed-variant: '#5b403c'
  secondary-fixed: '#fadcd2'
  secondary-fixed-dim: '#ddc1b7'
  on-secondary-fixed: '#271812'
  on-secondary-fixed-variant: '#56423b'
  tertiary-fixed: '#ece0dc'
  tertiary-fixed-dim: '#cfc4c0'
  on-tertiary-fixed: '#201a18'
  on-tertiary-fixed-variant: '#4c4542'
  background: '#121412'
  on-background: '#e2e3df'
  surface-variant: '#333533'
typography:
  display-lg:
    fontFamily: Libre Caslon Text
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  display-lg-mobile:
    fontFamily: Libre Caslon Text
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 38px
  headline-md:
    fontFamily: Libre Caslon Text
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.05em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  margin-mobile: 20px
  margin-desktop: 40px
  gutter: 16px
  stack-sm: 12px
  stack-md: 24px
  stack-lg: 48px
---

## Brand & Style

The brand personality is rooted in the "Slow Coffee" movement—warm, sophisticated, and deeply intentional. It targets coffee enthusiasts who view brewing as a ritual rather than a routine. The UI evokes the sensory experience of a high-end specialty cafe: the smell of freshly ground beans, the warmth of a ceramic mug, and the tactile nature of a paper journal.

The design style is a blend of **Minimalism** and **Tactile/Skeuomorphism**. In this dark mode expression, the interface shifts from an airy morning café to the intimate, moody atmosphere of a premium evening espresso bar. It uses heavy whitespace (as "negative dark space") to let high-quality photography breathe, while employing soft shadows and layered surfaces to create a sense of physical depth. The goal is a digital "coffee journal" aesthetic that feels timeless and premium.

## Colors

The palette is derived from the natural lifecycle of coffee, optimized for a sophisticated dark mode experience.
- **Primary (Espresso):** A deep, rich brown that forms the core of the brand. In dark mode, it acts as the anchor for primary actions and key structural elements.
- **Secondary (Roasted Bean):** A medium, warm taupe used for icons, secondary buttons, and active states, providing a soft glow against darker backgrounds.
- **Tertiary (Latte):** A soft, creamy beige used for text, subtle container fills, and borders to ensure high legibility.
- **Neutral (Parchment):** While the background is dark, this warm-toned neutral is used for high-contrast text and surface accents to mimic the feel of high-quality paper under low light.

Functional colors (Success, Error) should be desaturated to maintain the earthy harmony of the palette—think sage green for success and a muted terracotta for errors.

## Typography

This design system utilizes a high-contrast typographic pairing to balance heritage with modern utility.
- **Libre Caslon Text** is used for all headlines and display text. Its classic, editorial feel lends authority and an artisanal quality to the content.
- **Plus Jakarta Sans** provides a clean, modern counterpoint for body copy and labels. Its soft curves mirror the rounded UI elements, ensuring high readability during long-form reading.

In dark mode, font weights are carefully managed to prevent "glow" or blurring on dark backgrounds, maintaining wide line-heights for a relaxed, premium reading pace.

## Layout & Spacing

The layout follows a **fluid grid** model with generous safe areas to mimic the margins of a printed book.
- **Mobile:** A 4-column grid with 20px side margins.
- **Desktop:** A 12-column centered grid with a max-width of 1140px.

Spacing is calculated in increments of 8px. To emphasize the "cozy" feel, use larger vertical stacks (`stack-lg`) between distinct content sections to prevent the UI from feeling cluttered. Content should breathe; the dark background is treated as an expansive, quiet space.

## Elevation & Depth

In dark mode, depth is achieved through **Tonal Layers** rather than heavy shadows.
- **Surface Tiers:** The base background is the darkest layer. Elevated elements like cards or modals use progressively lighter brown-tinted greys or semi-transparent overlays of the Espresso color to appear closer to the user.
- **Shadow Character:** Use subtle, dark glows or very soft shadows (`rgba(0, 0, 0, 0.4)`) to separate layers. The goal is to make elements feel like they are objects sitting on a dark wooden table in a dimly lit room.
- **Micro-interactions:** When pressed, elements should appear to sink slightly (reduce shadow Y-offset) or dim in luminosity to reinforce the tactile, physical journal metaphor.

## Shapes

The shape language is organic and approachable, providing softness to the moody dark palette.
- **Standard Elements:** Use `rounded` (0.5rem) for input fields, small buttons, and tags.
- **Containers:** Use `rounded-lg` (1rem) for cards and primary content containers to give them a soft, welcoming appearance.
- **Feature Elements:** Use `rounded-xl` (1.5rem) for large image containers (e.g., hero shots of latte art) to emphasize the artisanal, non-corporate nature of the brand.

## Components

- **Buttons:** Primary buttons use the Espresso background with Tertiary text. They should have a subtle 1px inner border of Roasted Bean to add "edge" definition against dark backgrounds.
- **Cards:** Cards are defined by subtle tonal shifts from the background rather than borders. They are the primary vehicle for high-quality imagery; images should always be top-aligned with no margin to the card edge.
- **Inputs:** Text fields use a very dark brown fill and a bottom-only border (2px) in the Roasted Bean color, mimicking the lines of a notebook.
- **Chips/Tags:** Used for coffee notes (e.g., "Fruity," "Dark Roast"). These use soft, pill-shaped backgrounds in the Espresso color with Tertiary text.
- **Progress Bars:** For brewing timers, use a thick, rounded bar where the "filled" portion has a slight gradient from Roasted Bean to Espresso, resembling a liquid pour.
- **Images:** All photos of coffee should feature warm, low-key lighting and natural textures to complement the dark UI.