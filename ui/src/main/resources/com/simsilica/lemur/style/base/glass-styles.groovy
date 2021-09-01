package com.simsilica.lemur.style.base

import com.jme3.math.ColorRGBA
import com.simsilica.lemur.Button
import com.simsilica.lemur.Command
import com.simsilica.lemur.Insets3f
import com.simsilica.lemur.VAlignment
import com.simsilica.lemur.component.IconComponent
import com.simsilica.lemur.component.TbtQuadBackgroundComponent

def gradient = TbtQuadBackgroundComponent.create(
        texture( name:"/com/simsilica/lemur/icons/bordered-gradient.png",
                generateMips:false ),
        1, 1, 1, 126, 126,
        1f, false )

def menuHoverCommand = new Command<Button>() {
    void execute( Button source ) {
        if( source.isHighlightOn() ) {
            if (source.isEnabled()) {
                ((TbtQuadBackgroundComponent) source.getBackground()).setColor(color(0.25, 0.5, 0.5, 1.0))
            }
        } else {
            ((TbtQuadBackgroundComponent)source.getBackground()).setColor(color(0.25, 0.5, 0.5, 0.0))
        }
    }
}

def stdMenuCommands = [
        (Button.ButtonAction.HighlightOn) :[menuHoverCommand],
        (Button.ButtonAction.HighlightOff):[menuHoverCommand]
]

selector( "menu-bar", "glass") {
    background = gradient.clone()
    background.setMargin(5, 5)
    background.setColor(color(0.25, 0.5, 0.5, 1.0))
}

// Menu and MenuItem (Button)
selector( "menu-item", "glass") {
    background = gradient.clone()
    background.setColor(color(0.25, 0.5, 0.5, 0.0))
    background.setMargin(10, 5)
    color = color(0.8, 0.9, 1, 0.85f)
    buttonCommands = stdMenuCommands
}

// CheckboxMenuItem (CheckBox)
selector( "menu-checkbox", "glass" ) {

    def on = new IconComponent( "/com/simsilica/lemur/icons/Glass-check-on.png", 1f, 0, 0, 1f, false )
    on.setColor(color(0.5, 0.9, 0.9, 0.9))
    on.setMargin(5, 0)

    def off = new IconComponent( "/com/simsilica/lemur/icons/Glass-check-off.png", 1f, 0, 0, 1f, false )
    off.setColor(color(0.6, 0.8, 0.8, 0.8))
    off.setMargin(5, 0)

    onView = on
    offView = off

    background = gradient.clone()
    background.setColor(color(0.25, 0.5, 0.5, 0.0))
    background.setMargin(10, 5)

    buttonCommands = stdMenuCommands
}

// menu children container
selector( "menu-children", "glass" ) {
    background = gradient.clone()
    background.setMargin(5, 5)
    background.setColor(color(0.25, 0.5, 0.5, 1.0))
}


/////////// window

def pressedCommand = new Command<Button>() {
    void execute( Button source ) {
        if( source.isPressed() ) {
            source.move(1, -1, 0);
        } else {
            source.move(-1, 1, 0);
        }
    }
};

def stdButtonCommands = [
        (Button.ButtonAction.Down):[pressedCommand],
        (Button.ButtonAction.Up)  :[pressedCommand]
]

// WINDOW

selector( "window-title-bar", "glass" ) {
    // background = new QuadBackgroundComponent(ColorUtils.fromHex("#365d8f"))
    background = gradient.clone()
    background.setColor(color(0.25, 0.5, 0.5, 1.0))
    //background.setMargin(2, 2)
}

selector( "window-title-label", "glass" ) {
    background = null
    textVAlignment = VAlignment.Center
    color = new ColorRGBA(0.8, 0.8, 0.8, 1.0)
    insets = new Insets3f( 5, 5, 5, 15 )
}

selector( "window-content-outer", "glass") {
    background = gradient.clone()
    background.setColor(color(0.25, 0.5, 0.5, 1.0))
    background.setMargin(5,5)
}

selector( "window-content-inner", "glass" ) {
    background = gradient.clone()
    background.setColor(color(0.25, 0.5, 0.5, 0.25))
    background.setMargin(5,5)
}

selector( "window-button-minimize", "glass" ) {
    background = null
    icon = new IconComponent("Style/Icon/button.png")
    icon.setColor(color(1.0, 0.7412, 0.298, 1.0))
    insets = new Insets3f(5,5,5,5)
    fontSize = 0
}

selector( "window-button-maximize", "glass" ) {
    background = null
    icon = new IconComponent("Style/Icon/button.png")
    icon.setColor(color(0.0, 0.7921, 0.3411, 1.0))
    insets = new Insets3f(5,0,5,5)
    fontSize = 0
}

selector( "window-button-close", "glass" ) {
    background = null
    icon = new IconComponent("Style/Icon/button.png")
    icon.setColor(color(1.0, 0.3882, 0.3568, 1.0))
    insets = new Insets3f(5,0,5,5)
    fontSize = 0
}

selector( "dialog-button", "glass") {
    background = gradient.clone()
    color = color(0.8, 0.9, 1, 0.85f)
    background.setColor(color(0, 0.75, 0.75, 0.5))
    background.setMargin(15, 5)

    highlightColor = ColorRGBA.Yellow.clone()
    focusColor = ColorRGBA.Green.clone()

    insets = new Insets3f( 2, 2, 2, 2 );

    buttonCommands = stdButtonCommands;
}
