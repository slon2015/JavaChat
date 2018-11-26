import Core.Utils.UtilitHeaders;
import Models.Activity;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.ext.mail.LoginOption;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import lombok.val;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args){
        CommandLine cmd = parseCmd(args);
        if (cmd == null) return;

        String host = cmd.hasOption("host")?cmd.getOptionValue("host"): "localhost";
        int port = Integer.parseInt(cmd.hasOption("port")?cmd.getOptionValue("port"): "8080");
        final String emailAddress = cmd.getOptionValue("email");

        Vertx vertx = Vertx.vertx();
        HttpClientOptions clientOptions = new HttpClientOptions().setKeepAlive(false);
        val client = vertx.createHttpClient(clientOptions);

        client.get(port, host, "/utilits" )
                .putHeader(UtilitHeaders.UtilityName,"printactivities")
                .handler(response -> {
                    final List<Activity> activities = new ArrayList<>();

                    JsonParser jsonParser = JsonParser.newParser();
                    jsonParser.objectValueMode();
                    jsonParser.handler(event -> {
                        if( event.value() == null)
                            return;
                        Activity activity = event.mapTo(Activity.class);
                        activities.add(activity);
                    });


                    response.handler( buffer -> {
                        jsonParser.handle(buffer);
                    });

                    response.endHandler( v-> {
                        client.close();

                        if(emailAddress == null){
                            for( Activity activity : activities){
                                System.out.println(activity);
                            }
                            vertx.close();
                        }
                        else{
                            StringBuilder mailText = new StringBuilder();

                            mailText.append("Activities\n\n");

                            for( Activity activity : activities){
                                mailText.append(activity.toString());
                                mailText.append("\n");
                            }

                            String smtpHost = cmd.getOptionValue("smtpHost");
                            Integer smtpPort = Integer.parseInt( cmd.getOptionValue("smtpPort") );
                            String smtpLogin = cmd.getOptionValue("smtpLogin");
                            String smtpPassword = cmd.getOptionValue("smtpPassword");

                            if( smtpHost == null || smtpPort == null || smtpLogin == null || smtpPassword == null){
                                System.out.println("SMTP parameters not found");
                            }
                            else {

                                MailConfig config = new MailConfig();
                                config.setTrustAll(true);
                                config.setHostname(smtpHost);
                                config.setPort(smtpPort);
                                config.setLogin(LoginOption.REQUIRED);
                                config.setSsl(true);
                                config.setUsername(smtpLogin);
                                config.setPassword(smtpPassword);
                                val mailClient = MailClient.createNonShared(vertx, config);

                                MailMessage message = new MailMessage();
                                message.setSubject("Activities");
                                message.setFrom(emailAddress);
                                message.setTo(emailAddress);
                                message.setText(mailText.toString());
                                mailClient.sendMail(message, res -> {
                                    if (!res.succeeded()) {
                                        System.out.println("Mail sending failed");
                                        res.cause().printStackTrace();
                                    }
                                    else {
                                        System.out.println("Mail sent " + emailAddress);
                                    }
                                    vertx.close();
                                });
                            }
                        }
                    });
                }).end();
    }

    private static CommandLine parseCmd(String[] args) {
        Options options = getOptions();


        HelpFormatter helper = new HelpFormatter();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options,args);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        if(cmd.hasOption("h")){
            helper.printHelp("printActivities", options);
            return null;
        }
        return cmd;
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption(Option.builder("e").hasArg()
                .longOpt("email").desc("Email of activities responder").build());
        options.addOption(Option.builder("H").hasArg()
                .longOpt("host").desc("Host of chat's event bus").build());
        options.addOption(Option.builder("p").hasArg()
                .longOpt("port").desc("Port of chat's event bus").build());
        options.addOption(Option.builder("h")
                .longOpt("help").desc("Info about utility").build());
        options.addOption(Option.builder()
                .longOpt("smtpHost").hasArg().desc("SMTP server host").build());
        options.addOption(Option.builder()
                .longOpt("smtpPort").hasArg().desc("SMTP server port").build());
        options.addOption(Option.builder()
                .longOpt("smtpLogin").hasArg().desc("SMTP auth login").build());
        options.addOption(Option.builder()
                .longOpt("smtpPassword").hasArg().desc("SMTP auth password").build());
        return options;
    }
}
