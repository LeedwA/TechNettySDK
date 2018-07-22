package technetty.cwgj.com.technettysdk;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nettylib.netty.NettyClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NettyClient.getInstance().disConect();
                boolean isconect= NettyClient.getInstance().isChannerAlive();
                System.out.println("服务器连接状态："+isconect);
                connect();

            }
        },10*1000);
    }

    private void connect() {
        NettyClient.getInstance().start("47.97.189.126", "8099", 1000, new NettyClient.ConnectCallback() {
            @Override
            public void isConnect(boolean isConnect) {

            }
        }, new TestAdapter());
    }
}
