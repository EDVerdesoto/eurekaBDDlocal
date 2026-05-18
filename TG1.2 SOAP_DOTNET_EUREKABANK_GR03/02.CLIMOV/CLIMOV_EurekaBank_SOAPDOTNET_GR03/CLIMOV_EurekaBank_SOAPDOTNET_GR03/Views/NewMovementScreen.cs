using CLIMOV_EurekaBank_SOAPDOTNET_GR03.Services;
using Microsoft.Maui.Controls.Shapes;
using System.Globalization;

namespace CLIMOV_EurekaBank_SOAPDOTNET_GR03.Views
{
    public sealed class NewMovementScreen : ContentPage
    {
        private readonly EurekaBankClient _client;
        private readonly string _account;
        private readonly Picker _typePicker;
        private readonly Picker _destinationPicker;
        private readonly Entry _amountEntry;
        private readonly Label _destinationLabel;
        private readonly Label _messageLabel;
        private readonly ActivityIndicator _loading;
        private readonly Button _submitButton;

        public NewMovementScreen(EurekaBankClient client, string account)
        {
            _client = client;
            _account = account;
            BackgroundColor = Color.FromArgb("#F5F5F5");
            NavigationPage.SetHasNavigationBar(this, false);

            _typePicker = new Picker { Title = "Tipo de movimiento", TextColor = Color.FromArgb("#202124"), TitleColor = Color.FromArgb("#777777") };
            _typePicker.SelectedIndexChanged += OnTypeChanged;
            _destinationPicker = new Picker { Title = "Cuenta destino", TextColor = Color.FromArgb("#202124"), TitleColor = Color.FromArgb("#777777") };
            _amountEntry = new Entry
            {
                Placeholder = "Monto",
                Keyboard = Keyboard.Numeric,
                TextColor = Color.FromArgb("#202124"),
                PlaceholderColor = Color.FromArgb("#777777"),
                ClearButtonVisibility = ClearButtonVisibility.WhileEditing
            };
            _destinationLabel = Label("Cuenta destino", 13, "#4D4D4D");
            _messageLabel = new Label { FontSize = 14, HorizontalTextAlignment = TextAlignment.Center };
            _loading = new ActivityIndicator { Color = Color.FromArgb("#1976D2"), IsVisible = false };
            _submitButton = new Button
            {
                Text = "Registrar movimiento",
                HeightRequest = 54,
                CornerRadius = 10,
                FontAttributes = FontAttributes.Bold,
                BackgroundColor = Color.FromArgb("#1976D2"),
                TextColor = Colors.White
            };
            _submitButton.Clicked += OnSubmitClicked;

            Content = new Grid
            {
                RowDefinitions = { new RowDefinition(64), new RowDefinition(GridLength.Star) },
                Children =
                {
                    Header(),
                    new ScrollView
                    {
                        Content = new VerticalStackLayout
                        {
                            Padding = 20,
                            Spacing = 14,
                            Children =
                            {
                                Label($"Cuenta origen: {_account}", 18, "#1976D2", FontAttributes.Bold),
                                Label("Tipo", 13, "#4D4D4D"),
                                Field(_typePicker),
                                Label("Monto", 13, "#4D4D4D"),
                                Field(_amountEntry),
                                _destinationLabel,
                                Field(_destinationPicker),
                                _submitButton,
                                _loading,
                                _messageLabel
                            }
                        }
                    }.Row(1)
                }
            };
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await LoadFormAsync();
        }

        private async Task LoadFormAsync()
        {
            SetLoading(true);
            try
            {
                var types = await _client.GetMovementTypesAsync();
                _typePicker.ItemsSource = types;
                if (types.Count > 0)
                {
                    _typePicker.SelectedIndex = 0;
                }

                var accounts = await _client.GetAccountsAsync();
                _destinationPicker.ItemsSource = accounts
                    .Where(account => account.Codigo != _account && string.Equals(account.Estado, "ACTIVO", StringComparison.OrdinalIgnoreCase))
                    .Select(account => account.Codigo)
                    .ToList();

                UpdateDestinationVisibility();
            }
            catch (Exception ex)
            {
                ShowMessage($"No se pudo cargar el formulario: {ex.Message}", false);
            }
            finally
            {
                SetLoading(false);
            }
        }

        private async void OnSubmitClicked(object? sender, EventArgs e)
        {
            var type = _typePicker.SelectedItem?.ToString() ?? string.Empty;
            if (string.IsNullOrWhiteSpace(type))
            {
                ShowMessage("Seleccione un tipo de movimiento.", false);
                return;
            }

            if (!decimal.TryParse(_amountEntry.Text, NumberStyles.Number, CultureInfo.CurrentCulture, out var amount) &&
                !decimal.TryParse(_amountEntry.Text, NumberStyles.Number, CultureInfo.InvariantCulture, out amount))
            {
                ShowMessage("Ingrese un monto valido.", false);
                return;
            }

            var destination = IsTransfer(type) ? _destinationPicker.SelectedItem?.ToString() ?? string.Empty : string.Empty;
            if (IsTransfer(type) && string.IsNullOrWhiteSpace(destination))
            {
                ShowMessage("Seleccione la cuenta destino.", false);
                return;
            }

            SetLoading(true);
            try
            {
                var result = await _client.ProcessMovementAsync(type, _account, destination, amount);
                if (!result.Success)
                {
                    ShowMessage(result.Mensaje, false);
                    return;
                }

                await DisplayAlert("Movimiento registrado", result.Mensaje, "Aceptar");
                await Navigation.PopAsync();
            }
            catch (Exception ex)
            {
                ShowMessage($"No se pudo registrar: {ex.Message}", false);
            }
            finally
            {
                SetLoading(false);
            }
        }

        private void OnTypeChanged(object? sender, EventArgs e) => UpdateDestinationVisibility();

        private void UpdateDestinationVisibility()
        {
            var isTransfer = IsTransfer(_typePicker.SelectedItem?.ToString() ?? string.Empty);
            _destinationLabel.IsVisible = isTransfer;
            _destinationPicker.IsVisible = isTransfer;
        }

        private static bool IsTransfer(string type) => type.Equals("TRANSFERENCIA", StringComparison.OrdinalIgnoreCase);

        private void SetLoading(bool loading)
        {
            _loading.IsRunning = loading;
            _loading.IsVisible = loading;
            _submitButton.IsEnabled = !loading;
        }

        private void ShowMessage(string message, bool success)
        {
            _messageLabel.Text = message;
            _messageLabel.TextColor = success ? Color.FromArgb("#388E3C") : Color.FromArgb("#D32F2F");
        }

        private Grid Header()
        {
            var header = new Grid
            {
                BackgroundColor = Color.FromArgb("#1976D2"),
                Padding = new Thickness(8, 0),
                ColumnDefinitions = { new ColumnDefinition(GridLength.Auto), new ColumnDefinition(GridLength.Star) }
            };
            var back = new Button { Text = "<", BackgroundColor = Colors.Transparent, TextColor = Colors.White, FontSize = 24, WidthRequest = 48 };
            back.Clicked += async (_, _) => await Navigation.PopAsync();
            header.Add(back, 0);
            header.Add(new Label
            {
                Text = "Nuevo movimiento",
                TextColor = Colors.White,
                FontSize = 20,
                FontAttributes = FontAttributes.Bold,
                HorizontalTextAlignment = TextAlignment.Center,
                VerticalTextAlignment = TextAlignment.Center
            }, 1);
            return header;
        }

        private static Border Field(View content) => new()
        {
            Stroke = Color.FromArgb("#BDBDBD"),
            StrokeThickness = 1,
            BackgroundColor = Colors.White,
            Padding = new Thickness(12, 0),
            StrokeShape = new RoundRectangle { CornerRadius = 4 },
            Content = content
        };

        private static Label Label(string text, double size, string color, FontAttributes attributes = FontAttributes.None) => new()
        {
            Text = text,
            FontSize = size,
            TextColor = Color.FromArgb(color),
            FontAttributes = attributes
        };
    }
}
