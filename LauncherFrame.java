/* Decompiler 18ms, total 119ms, lines 100 */
package net.minecraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LauncherFrame extends Frame {
   public static final int VERSION = 12;
   private static final long serialVersionUID = 1L;
   private Launcher launcher;
   private LoginForm loginForm;
   public boolean forceUpdate = false;

   public LauncherFrame() {
      super("Minecraft Launcher (by AnjoCaido)");
      System.out.println("Hello!");
      this.setBackground(Color.BLACK);
      this.loginForm = new LoginForm(this);
      this.setLayout(new BorderLayout());
      this.add(this.loginForm, "Center");
      this.loginForm.setPreferredSize(new Dimension(854, 480));
      this.pack();
      this.setLocationRelativeTo((Component)null);

      try {
         this.setIconImage(ImageIO.read(LauncherFrame.class.getResource("favicon.png")));
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent arg0) {
            (new Thread() {
               public void run() {
                  try {
                     Thread.sleep(30000L);
                  } catch (InterruptedException var2) {
                     var2.printStackTrace();
                  }

                  System.out.println("FORCING EXIT!");
                  System.exit(0);
               }
            }).start();
            if (LauncherFrame.this.launcher != null) {
               LauncherFrame.this.launcher.stop();
               LauncherFrame.this.launcher.destroy();
            }

            System.exit(0);
         }
      });
   }

   public String getFakeResult(String userName) {
      return MinecraftUtil.getFakeLatestVersion() + ":35b9fd01865fda9d70b157e244cf801c:" + userName + ":12345:";
   }

   public void login(String userName) {
      String result = this.getFakeResult(userName);
      String[] values = result.split(":");
      this.launcher = new Launcher();
      this.launcher.forceUpdate = this.forceUpdate;
      this.launcher.customParameters.put("userName", values[2].trim());
      this.launcher.customParameters.put("sessionId", values[3].trim());
      this.launcher.init();
      this.removeAll();
      this.add(this.launcher, "Center");
      this.validate();
      this.launcher.start();
      this.loginForm.loginOk();
      this.loginForm = null;
      this.setTitle("Minecraft");
   }

   private void showError(String error) {
      this.removeAll();
      this.add(this.loginForm);
      this.loginForm.setError(error);
      this.validate();
   }

   public boolean canPlayOffline(String userName) {
      Launcher launcher2 = new Launcher();
      launcher2.init(userName, "12345");
      return launcher2.canPlayOffline();
   }

   public static void main(String[] args) {
      LauncherFrame launcherFrame = new LauncherFrame();
      launcherFrame.setVisible(true);
   }
}
