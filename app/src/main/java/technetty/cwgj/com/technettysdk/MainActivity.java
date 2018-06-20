package technetty.cwgj.com.technettysdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nettylib.netty.NettyClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NettyClient.getInstance().start("118.25.65.75", "8099", 1000, new NettyClient.ConnectCallback() {
            @Override
            public void isConnect(boolean isConnect) {

            }
        }, new TestAdapter());
    }
}
