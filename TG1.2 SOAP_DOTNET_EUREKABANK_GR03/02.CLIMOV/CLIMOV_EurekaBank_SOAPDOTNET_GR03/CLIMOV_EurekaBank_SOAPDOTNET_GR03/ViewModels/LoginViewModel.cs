using System.ComponentModel;
using System.Windows.Input;
using CLIMOV_EurekaBank_SOAPDOTNET_GR03.Services;
using CLIMOV_EurekaBank_SOAPDOTNET_GR03.Views;

namespace CLIMOV_EurekaBank_SOAPDOTNET_GR03.ViewModels
{
    public class LoginViewModel : INotifyPropertyChanged
    {
        private string _usuario = string.Empty;
        private string _clave = string.Empty;
        private bool _isLoading;
        private string _mensaje = string.Empty;
        private bool _esExitoso;

        public string Usuario
        {
            get => _usuario;
            set { _usuario = value; OnPropertyChanged(nameof(Usuario)); }
        }

        public string Clave
        {
            get => _clave;
            set { _clave = value; OnPropertyChanged(nameof(Clave)); }
        }

        public bool IsLoading
        {
            get => _isLoading;
            set { _isLoading = value; OnPropertyChanged(nameof(IsLoading)); }
        }

        public string Mensaje
        {
            get => _mensaje;
            set { _mensaje = value; OnPropertyChanged(nameof(Mensaje)); }
        }

        public bool EsExitoso
        {
            get => _esExitoso;
            set { _esExitoso = value; OnPropertyChanged(nameof(EsExitoso)); }
        }

        public ICommand LoginCommand { get; }

        public LoginViewModel()
        {
            LoginCommand = new Microsoft.Maui.Controls.Command(async () =>
            {
                if (string.IsNullOrWhiteSpace(Usuario) || string.IsNullOrWhiteSpace(Clave))
                {
                    EsExitoso = false;
                    Mensaje = "Complete todos los campos";
                    return;
                }

                IsLoading = true;
                Mensaje = string.Empty;
                await Task.Delay(250);
                IsLoading = false;
                EsExitoso = true;
                Mensaje = "Inicio de sesión exitoso";

                if (Application.Current?.MainPage is NavigationPage navigationPage)
                {
                    await navigationPage.Navigation.PushAsync(new AccountsScreen(new EurekaBankClient()));
                }
            });
        }

        public event PropertyChangedEventHandler? PropertyChanged;

        protected void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
