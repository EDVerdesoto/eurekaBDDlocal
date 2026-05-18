namespace CLIMOV_EurekaBank_RESTDOTNET_GR03
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();
        }
        protected override Window CreateWindow(IActivationState? activationState)
        {
            return new Window(new NavigationPage(new Views.LoginScreen())
            {
                BarBackgroundColor = Color.FromArgb("#7C3AED"),
                BarTextColor = Colors.White
            });
        }
    }
}
