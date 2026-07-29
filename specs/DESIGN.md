---
name: Artisanal Brew Narrative
colors:
  surface: '#faf9f5'
  surface-dim: '#dadad6'
  surface-bright: '#faf9f5'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f4f4f0'
  surface-container: '#eeeeea'
  surface-container-high: '#e8e8e4'
  surface-container-highest: '#e2e3df'
  on-surface: '#1a1c1a'
  on-surface-variant: '#504442'
  inverse-surface: '#2f312e'
  inverse-on-surface: '#f1f1ed'
  outline: '#827472'
  outline-variant: '#d3c3c0'
  surface-tint: '#745853'
  primary: '#271310'
  on-primary: '#ffffff'
  primary-container: '#3e2723'
  on-primary-container: '#ae8d87'
  inverse-primary: '#e3beb8'
  secondary: '#6f5a52'
  on-secondary: '#ffffff'
  secondary-container: '#fadcd2'
  on-secondary-container: '#766057'
  tertiary: '#1d1815'
  on-tertiary: '#ffffff'
  tertiary-container: '#322c29'
  on-tertiary-container: '#9c938f'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
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
  background: '#faf9f5'
  on-background: '#1a1c1a'
  surface-variant: '#e2e3df'
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

The design style is a blend of **Minimalism** and **Tactile/Skeuomorphism**. It uses heavy whitespace to let high-quality photography breathe, while employing soft shadows and layered surfaces to create a sense of physical depth. The goal is a digital "coffee journal" aesthetic that feels timeless and premium.

## Colors

The palette is derived from the natural lifecycle of coffee.
- **Primary (Espresso):** A deep, rich brown used for primary actions, headlines, and high-contrast elements.
- **Secondary (Roasted Bean):** A medium, warm taupe used for icons, secondary buttons, and active states.
- **Tertiary (Latte):** A soft, creamy beige used for subtle backgrounds, container fills, and borders.
- **Neutral (Parchment):** An off-white, warm-toned background color that reduces eye strain and mimics the feel of high-quality paper.

Functional colors (Success, Error) should be desaturated to maintain the earthy harmony of the palette—think sage green for success and a muted terracotta for errors.

## Typography

This design system utilizes a high-contrast typographic pairing to balance heritage with modern utility.
- **Libre Caslon Text** is used for all headlines and display text. Its classic, editorial feel lends authority and an artisanal quality to the content.
- **Plus Jakarta Sans** provides a clean, modern counterpoint for body copy and labels. Its soft curves mirror the rounded UI elements, ensuring high readability during long-form reading of brewing guides or bean descriptions.

Use wide line-heights for body text to maintain a relaxed, premium reading pace.

## Layout & Spacing

The layout follows a **fluid grid** model with generous safe areas to mimic the margins of a printed book.
- **Mobile:** A 4-column grid with 20px side margins.
- **Desktop:** A 12-column centered grid with a max-width of 1140px.

Spacing is calculated in increments of 8px. To emphasize the "cozy" feel, use larger vertical stacks (`stack-lg`) between distinct content sections to prevent the UI from feeling cluttered. Content should breathe; do not be afraid of "empty" parchment space.

## Elevation & Depth

Depth is achieved through **Tonal Layers** and **Ambient Shadows**.
- **Surface Tiers:** The base layer is the Neutral (Parchment). Elevated elements like cards or modals use a pure white or a very light cream tint.
- **Shadow Character:** Use extremely soft, diffused shadows with a slight brown tint (`rgba(62, 39, 35, 0.08)`) instead of pure gray. This makes the elements feel like they are resting on paper rather than floating in digital space.
- **Micro-interactions:** When pressed, elements should appear to sink slightly (reduce shadow Y-offset) to reinforce the tactile, physical journal metaphor.

## Shapes

The shape language is organic and approachable.
- **Standard Elements:** Use `rounded` (0.5rem) for input fields, small buttons, and tags.
- **Containers:** Use `rounded-lg` (1rem) for cards and primary content containers to give them a soft, welcoming appearance.
- **Feature Elements:** Use `rounded-xl` (1.5rem) for large image containers (e.g., hero shots of latte art) to emphasize the artisanal, non-corporate nature of the brand.

## Components

- **Buttons:** Primary buttons use the Espresso background with Parchment text. They should have a subtle 1px inner border of a slightly lighter brown to add "edge" definition.
- **Cards:** Cards should be borderless but elevated by the ambient brown-tinted shadow. They are the primary vehicle for high-quality imagery; images should always be top-aligned with no margin to the card edge.
- **Inputs:** Text fields use a very light Tertiary fill and a bottom-only border (2px) in the secondary color, mimicking the lines of a notebook.
- **Chips/Tags:** Used for coffee notes (e.g., "Fruity," "Dark Roast"). These use soft, pill-shaped backgrounds in the Tertiary color with Espresso text.
- **Progress Bars:** For brewing timers, use a thick, rounded bar where the "filled" portion has a slight gradient from Roasted Bean to Espresso, resembling a liquid pour.
- **Images:** All photos of coffee should feature warm lighting and natural textures (wood tables, ceramic mugs, linen napkins).