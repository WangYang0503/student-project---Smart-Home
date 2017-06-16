using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;


// The Blank Page item template is documented at http://go.microsoft.com/fwlink/?LinkId=402352&clcid=0x409

namespace LightControlUWP
{
  /// <summary>
  /// An empty page that can be used on its own or navigated to within a Frame.
  /// </summary>
  public sealed partial class MainPage : Page
  {
    public MainPage()
    {
      this.InitializeComponent();
      LightControl myLightControl = new LightControl();
      LED myLed1 = new LED("LED1", 2, 3, 4);
      myLightControl.addLight(myLed1);
      LED myLed2 = new LED("LED2", 17, 27, 22);
      myLightControl.addLight(myLed2);
      myLed1.On = true;
      myLed1.On = false;
      myLed2.On = true;
      myLed2.On = false;
      myLightControl.setAll(true);
      myLightControl.setAll(false);

    }
  }
}
