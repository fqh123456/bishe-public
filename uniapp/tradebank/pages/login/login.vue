<template>
	<view>
		<view >
			<u-avatar class="avatar"  size="60" ></u-avatar>
			<u--text v-if="user.name!=null" align="center" :text=text></u--text>
			<u--text v-if="user.name==null" align="center" text="亲爱的用户您好"></u--text>
		</view>
		<u-keyboard @cancel="cancel"
		 @change="valchange" 
		 @close="close" 
		 @backspace="backspace"
		 @confirm="confirm"
		 ref="uKeyboard"
		 mode="number" 
		 :show="show"></u-keyboard>
		<u--input
		    
		    placeholder="请输入账号"
		    border="bottom"
		    v-model="account"
			class="input"
		  ></u--input>
		  <u--input
		      placeholder="请输入密码"
			  type="password"
		      border="surround"
		      v-model="password"
		      @focus="change"
			  
			  class="input"
		    >
			  <template slot="suffix">
			  					<u-button
								    text="忘记密码"
			  						@click="forgetpassword()"
			  						size="mini"
			  					></u-button>
			  				</template>
			</u--input>
			<u-button
			 type="primary" 
			 text="登录" 
			 
			 @click="login"
			 class="button"
			></u-button>
			<u-toast ref="uToast"></u-toast>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				account: '',
				password: '',
				text:'',
				user:{},
				show:false
			};
		},
		onLoad() {
			this.getuser()
		},
		methods:{
			getuser(){
				this.user=uni.getStorageSync('user')
				this.text='亲爱的'+this.user.name+'您好'
			},
//数字键盘的操作=========================================================================
			change(){
				this.show=true
			},
			close(){
				this.show=false
			},
			cancel(){
				this.show=false
				this.password=""
			},
			valchange(val){
				this.password+=val
			},
			backspace(){
				if(this.password.length){
					this.password=this.password.substr(0,this.password.length-1)
				}
			},
			confirm(){
				this.show=false
				this.login()
			},
//分割=================================================================================
			login(){
				this.request({
					url:"/User/login",
					method:"POST",
					data:{
						account:this.account,
						password:this.password
					}
				}).then(res=>{
					if(res.code==='200'){
						uni.setStorageSync('user',res.data)
						this.$refs.uToast.show({
											type:'success',
											duration:'800',
											message:"登陆成功",
										})
						this.password=""
						this.setlogintime(res.data.id)
					}
					else if(res.code==='404'){
						this.$refs.uToast.show({
											type:'error',
											duration:'800',
											message:"错误！该账户不存在",
										})
					}
					else if(res.code==='500'){
						this.$refs.uToast.show({
											type:'error',
											duration:'800',
											message:"账号或密码错误！",
										})
					}
				})
			},
		    forgetpassword(){
				alert("忘记了密码");
			},
			setlogintime(val){
				this.request({
					url:"/User/setLoginTime",
					method:"POST",
					data:{
						id:val,
					}
				}).then(res=>{
					uni.reLaunch({
						url:"/pages/index/index"
					})
				})
			}
		}
	}
</script>

<style lang="scss">
.input{
	width: 60%;
	margin:2% auto;
}
.button{
	width: 80%;
	margin: 2% auto;
}
.avatar{
	margin: 3% auto;
	
	}
</style>
