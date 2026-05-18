using System.ComponentModel;
using CLIMOV_EurekaBank_RESTDOTNET_GR03.Services;

namespace CLIMOV_EurekaBank_RESTDOTNET_GR03.ViewModels
{
    public class MovementsViewModel : INotifyPropertyChanged
    {
        private readonly EurekaBankClient _client;
        private readonly string _cuenta;
        private List<MovementSummary> _movements = [];
        private bool _isLoading;
        private string _mensaje = "";

        public List<MovementSummary> Movements { get => _movements; set { _movements = value; OnPropertyChanged(nameof(Movements)); } }
        public bool IsLoading { get => _isLoading; set { _isLoading = value; OnPropertyChanged(nameof(IsLoading)); } }
        public string Mensaje { get => _mensaje; set { _mensaje = value; OnPropertyChanged(nameof(Mensaje)); } }

        public MovementsViewModel(EurekaBankClient client, string cuenta)
        {
            _client = client;
            _cuenta = cuenta;
        }

        public async Task LoadMovementsAsync()
        {
            IsLoading = true;
            Mensaje = "";
            try
            {
                Movements = await _client.GetMovementsAsync(_cuenta);
                if (Movements.Count == 0) Mensaje = "No hay movimientos.";
            }
            catch (Exception ex)
            {
                Mensaje = $"Error: {ex.Message}";
            }
            finally { IsLoading = false; }
        }

        public event PropertyChangedEventHandler? PropertyChanged;
        protected void OnPropertyChanged(string name) => PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
    }
}
