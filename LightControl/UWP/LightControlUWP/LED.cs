using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Devices.Gpio;

namespace LightControlUWP
{
  class LED
  {
    private const GpioPinDriveMode driveMode = GpioPinDriveMode.Output;

    public String Name { get; private set; }

    public Boolean On
    {
      get { return On; }
      set
      {
        setPin(value ? GpioPinValue.High : GpioPinValue.Low);
      }
    }

    Byte red;
    Byte green;
    Byte blue;

    private GpioPin pRed;
    private GpioPin pGreen;
    private GpioPin pBlue;

    private GpioController myController;

    public LED(String name, int pinRed, int pinGreen, int pinBlue)
    {
      Name = name;
      myController = GpioController.GetDefault();
      pRed = myController.OpenPin(pinRed);
      pRed.SetDriveMode(driveMode);
      pGreen = myController.OpenPin(pinGreen);
      pGreen.SetDriveMode(driveMode);
      pBlue = myController.OpenPin(pinBlue);
      pBlue.SetDriveMode(driveMode);
      On = false;
    }

    private void setPin(GpioPinValue value)
    {
      pRed.Write(value);
      pGreen.Write(value);
      pBlue.Write(value);
    }
  }
}
