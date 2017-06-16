using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Devices.Gpio;
using Windows.Devices.Pwm;

namespace LightControlUWP
{
  class LightControl
  {
    Collection<LED> allLights;

    public LightControl()
    {
      allLights = new Collection<LED>();
    }

    public void addLight(LED light)
    {
      allLights.Add(light);
    }

    public String getAllLightNames()
    {
      String retVal = "";
      
      foreach (LED light in allLights)
      {
        retVal += light.Name;
        retVal += "; ";
      }

      return retVal;
    }

    public void setAll(Boolean on)
    {
      foreach (LED light in allLights)
      {
        light.On = on;
      }
    }

    public Boolean setLight(String name, Boolean on)
    {
      Boolean retVal = false;

      foreach (LED light in allLights)
      {
        if (((String)light.Name).Equals(name,StringComparison.CurrentCultureIgnoreCase))
        {
          light.On = on;
          retVal = true;
        }
      }

      return retVal;
    }
  }
}
