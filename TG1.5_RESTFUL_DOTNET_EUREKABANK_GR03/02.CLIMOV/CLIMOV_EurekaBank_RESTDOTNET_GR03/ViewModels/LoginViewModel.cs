using System.ComponentModel;
using System.Windows.Input;
using CLIMOV_EurekaBank_RESTDOTNET_GR03.Services;
using CLIMOV_EurekaBank_RESTDOTNET_GR03.Views;

namespace CLIMOV_EurekaBank_RESTDOTNET_GR03.ViewModels
{
    public class LoginViewModel : INotifyPropertyChanged
    {
        private string _usuario = "";
        private string _clave = "";
        private bool _isLoading;
        private string _mensaje = "";
        private bool _esExitoso;

        public string Usuario { get => _usuario; set { _usuario = value; OnPropertyChanged(nameof(Usuario)); } }
        public string Clave { get => _clave; set { _clave = value; OnPropertyChanged(nameof(Clave)); } }
        public bool IsLoading { get => _isLoading; set { _isLoading = value; OnPropertyChanged(nameof(IsLoading)); } }
        public string Mensaje { get => _mensaje; set { _mensaje = value; OnPropertyChanged(nameof(Mensaje)); } }
        public bool EsExitoso { get => _esExitoso; set { _esExitoso = value; OnPropertyChanged(nameof(EsExitoso)); } }

        public ICommand LoginCommand { get; }

        public LoginViewModel()
        {
            LoginCommand = new Command(async () =>
            {
                if (string.IsNullOrWhiteSpace(Usuario) || string.IsNullOrWhiteSpace(Clave))
                {
                    EsExitoso = false;
                    Mensaje = "Complete todos los campos";
                    return;
                }
                IsLoading = true;
                Mensaje = "";
                try
                {
                    var client = new EurekaBankClient();
                    var ok = await client.LoginAsync(Usuario.Trim(), Clave);
                    IsLoading = false;
                    if (ok)
                    {
                        EsExitoso = true;
                        Mensaje = "Inicio de sesion exitoso";
                        if (Application.Current?.MainPage is NavigationPage nav)
                            await nav.Navigation.PushAsync(new AccountsScreen(client));
                    }
                    else
                    {
                        EsExitoso = false;
                        Mensaje = "Usuario o clave incorrectos";
                    }
                }
                catch (Exception ex)
                {
                    IsLoading = false;
                    EsExitoso = false;
                    Mensaje = $"Error: {ex.Message}";
                }
            });
        }

        public event PropertyChangedEventHandler? PropertyChanged;
        protected void OnPropertyChanged(string name) => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}
