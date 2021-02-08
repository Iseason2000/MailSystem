package top.iseason.BookMail.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import top.iseason.BookMail.Manager.MailManager;
import top.iseason.BookMail.Util.Message;
import top.iseason.BookMail.Util.SimpleSubCommand;
import top.iseason.BookMail.myclass.Mail;

import java.util.List;

public class MailSendCommand extends SimpleSubCommand {
    MailSendCommand(String command) {
        super(command);
        setUsage("send [玩家]");
        setDescription("将手上的成书转为邮件并发送");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家才能使用这个命令");
            return;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            Message.send(player, ChatColor.YELLOW + "请填写需要发送的人！");
            Message.send(player, ChatColor.GREEN + "/bookmail send [玩家]");
            return;
        }
        if (!player.isOp() && args.length > 1) return;
        Mail mail = MailManager.getMailInHand(player);
        if(mail == null) return;
        sendMail(player,mail,args);
        if (!player.isOp()) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getAmount() == 1) {
                int slot = player.getInventory().getHeldItemSlot();
                player.getInventory().setItem(slot, null);
            }
            item.setAmount(item.getAmount() - 1);

        }
    }
    public static void sendMail(Player player,Mail mail,String[] players){
        List<String> failureList = MailManager.sendMailtoPlayers(mail, players);
        if (failureList.size() > 0) {
            StringBuilder failureNames = new StringBuilder();
            for (String name : failureList) {
                failureNames.append(" ").append(name);
            }
            Message.send(player, ChatColor.YELLOW + "邮件已发送，但有 " + ChatColor.RED + failureList.size() + ChatColor.YELLOW + " 人发送失败!" + ChatColor.LIGHT_PURPLE + "他们分别是:");
            Message.send(player, ChatColor.YELLOW + failureNames.toString());
        } else {
            Message.send(player, ChatColor.GREEN + "邮件全部发送成功!");
        }
    }

}
