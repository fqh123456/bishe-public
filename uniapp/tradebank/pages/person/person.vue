<template>
	<view  style="padding: 2%;" >
		<view class="box"  >
			
			<view style="display: flex; padding: 5%;" >
				<u-avatar  size="60" class="avatar" ></u-avatar>
				<u--text class="text1" mode="name"  format="encrypt" bold :text="user.name"></u--text>
				<view style="flex: 1;">
					<u--text prefixIcon="integral" iconStyle="font-size: 15px"  align="right"  bold size="12" text="个人账户"></u--text>
					<u--text    align="right" size="15" text="上次登录"></u--text>
					<u--text  style="margin-bottom: 12%;" align="right"  size="12" :text="user.lasttime"></u--text>
				</view>
			</view>
		</view>
		<view style="width: 80%;margin: 5% auto;">
			<u-cell-group>
					<u-cell icon="lock-opened-fill" title="登录密码" @click="openchangePassword"></u-cell>
					<u-cell icon="integral-fill" title="会员等级" value="新版本"></u-cell>
			</u-cell-group>
		</view>
		<view style="width: 50%; margin: 5% auto" >
			<u-button shape="circle" text="安全退出" @click="outlogin()"></u-button>
		</view>
		
		<view >
			<u-modal @confirm="checkCode" @cancel="cancel" :show="show" showCancelButton="true" >
				<view style="width: 100%;">
						<u--form
								labelPosition="left"
								:model="model"
								ref="uForm"
						>
							<u-form-item
							        labelWidth="20%"
									label="原密码"
									borderBottom
									ref="item1"
							>
								<u--input
										v-model="model.userInfo.password"
										border="none"
										type="number"
										password=true
								></u--input>
							</u-form-item>
							<u-form-item
							        labelWidth="20%"
									label="新密码"
									borderBottom
									ref="item1"
							>
								<u--input
								        type="number"
								        password=true
										v-model="model.userInfo.newpassword"
										border="none"
								></u--input>
							</u-form-item>
							<u-form-item
							        labelWidth="30%"
									label="确认新密码"
									borderBottom
									ref="item1"
							>
								<u--input
								        type="number"
										password=true
										v-model="model.userInfo.newpassword2"
										border="none"
								></u--input>
							</u-form-item>
							<u-form-item
							        labelWidth="20%"
									label="验证码"
									borderBottom
									ref="item1"
							>
								<u-code-input size="20" style="width: 85%;margin-top: 5%;" v-model="model.userInfo.code">
									
								</u-code-input>
							</u-form-item>
							<u-form-item
									borderBottom
									ref="item1"
							>
								<template  >
													<u-code
														ref="uCode"
														@change="codeChange"
														seconds="60"
														changeText="X秒重新获取"
													></u-code>
													<u-button
														@tap="getCode"
														:text="tips"
														type="success"
														size="mini"
													></u-button>
												</template>
							</u-form-item>
						</u--form>
						
				 </view>
				 <u-toast ref="uToast2"></u-toast>
			</u-modal>
		</view>
			<u-toast ref="uToast"></u-toast>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				user:uni.getStorageSync('user'),
				show:false,
				model:{userInfo:{
					password:"",
					newpassword:"",
					newpassword2:"",
					code:"",
				}},
				tips:'',
			};
		},
		onLoad() {
			if(this.user.lasttime==null){
				this.user.lasttime="未知"
			}
		},
		methods:{
			outlogin(){//安全退出
				uni.removeStorageSync('user')
				uni.removeStorageSync('userId')
				uni.removeStorageSync('bankdetail')
				uni.reLaunch({
					url:"/pages/login/login"
				})
			},
			openchangePassword(){//打开密码更改页面
				this.show=true
			},
			cancel(){//取消更改密码
				this.show=false
				this.model.userInfo={password:"",newpassword:"",newpassword2:"",code:"",}
			},
			codeChange(text) {//验证码输入
			         this.tips = text;
			       },
			getCode() {//发送验证码前的验证
			      if(this.model.userInfo.password!=this.user.password){
			      	uni.$u.toast('原密码错误！');
					this.model.userInfo.password=''
			      }
			      else if(this.model.userInfo.newpassword!=this.model.userInfo.newpassword2){
			      	uni.$u.toast('两次密码不一致！');
			      }
			      else{
			      	if (this.$refs.uCode.canGetCode) {
			      	  this.sentCode();
			      	} else {
			      	  uni.$u.toast('倒计时结束后再发送');
			      	}
			      }},
			sentCode(){//发送验证码
				      uni.showLoading({
				        title: '正在获取验证码'
				      })
				      this.request({
				      	url:"/Code/setCode",
				      	method:"POST",
				      	data:{
				      		uid:uni.getStorageSync('user').id,
				      		phone:uni.getStorageSync('user').phone
				      	}
				      }).then(res=>{
				      	uni.hideLoading();
				      	// 这里此提示会被this.start()方法中的提示覆盖
				      	if(res.code==='200'){
				      		uni.$u.toast('验证码已发送,5分钟内有效');
				      		// 通知验证码组件内部开始倒计时
				      		this.$refs.uCode.start();
				      	}
				      	else{
				      		uni.$u.toast('验证码发送失败！请稍后再试');
				      	}
			       })},
			checkCode(){//检验验证码
				if(this.model.userInfo.password!=this.user.password){
					uni.$u.toast('原密码错误！');
									this.model.userInfo.password=''
				}
				else if(this.model.userInfo.newpassword!=this.model.userInfo.newpassword2){
					uni.$u.toast('两次密码不一致！');
				}
				else if(this.model.userInfo.password==''||this.model.userInfo.newpassword==''||this.model.userInfo.newpassword2==''){
					this.$refs.uToast2.show({
										duration:'1200',
										message:"请将信息填写完整！",
									})
				}
				else if(this.model.userInfo.code.length<6){
					this.$refs.uToast2.show({
										duration:'1200',
										message:"请填写6位的验证码！",
									})
				}
				else{
					this.request({
						url:"/Code/checkCode",
						method:"POST",
						data:{
							phoneCode:this.model.userInfo.code,
							uid:uni.getStorageSync('user').id
						}
					}).then(res=>{
						this.model.userInfo.code=''
						if(res.code==='200'){
							 this.changePassword()
						}
						else if(res.code==='404'){
							this.$refs.uToast2.show({
												type:'error',
												duration:'800',
												message:"验证码错误！",
											})
						}
						else if(res.code==='300'){
							this.$refs.uToast2.show({
												type:'warn',
												duration:'800',
												message:"验证码已失效！请重新发送",
											})
						}
						
					})
				}
			},
			changePassword(){//确认向后端修改密码
				this.request({
					url:"/User/changePassword",
					method:"POST",
					data:{
						newpassword:this.model.userInfo.newpassword,
						uid:uni.getStorageSync('user').id
					}
				}).then(res=>{
					if(res.code==='200'){
						this.show=false
						this.$refs.uToast.show({
											type:'success',
											duration:'2500',
											message:"密码修改成功！请重新登录",
											complete() {
												uni.removeStorageSync('user')
												uni.removeStorageSync('userId')
												uni.navigateTo({
													url: "/pages/login/login"
												})
											}					
										})
						uni.getStorageSync('user').password=this.model.userInfo.newpassword
						this.model.userInfo={password:"",newpassword:"",newpassword2:"",code:"",
				         }
					}
					else{
						this.$refs.uToast2.show({
											type:'error',
											duration:'1000',
											message:"密码修改失败！请联系客服或者稍后再试",
										})
					}
				})
			}
			
		}
	}
</script>

<style lang="scss">
.avatar{
	margin: 0 2%;
}
.text1{
	padding-left: 1%;
	padding-bottom: 10%;
	flex: 1;
}
.box{
	margin: 5% auto;
	width: 100%;
	border: 1px solid #ddd;
	border-radius: 5px;
	box-shadow: 0 2px 5px rgba(0,0,0,0.1);
	background-image: url(~@/static/image/inset/background.jpg);
	background-size: 100% 100%;
	background-position: 50% 50%;
	background-repeat: no-repeat;
}
</style>
