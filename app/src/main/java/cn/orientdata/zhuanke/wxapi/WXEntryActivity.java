package cn.orientdata.zhuanke.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "WXEntryActivity";
	private IWXAPI api;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.api = WXAPIFactory.createWXAPI(this, "wx055b79a81a71b0cb");
		if (api.handleIntent(getIntent(), this)) {
			WXEntryActivity.this.finish();
		}
	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		this.api.handleIntent(intent, this);
	}

	public void onReq(BaseReq arg0) {

	}

	public void onResp(BaseResp resp) {

        switch (resp.getType()){
            case ConstantsAPI.COMMAND_SENDAUTH: //登录回调
                Toast.makeText(WXEntryActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX: //分享回调
				Logger.d("分享成功");
                Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

	}
}
