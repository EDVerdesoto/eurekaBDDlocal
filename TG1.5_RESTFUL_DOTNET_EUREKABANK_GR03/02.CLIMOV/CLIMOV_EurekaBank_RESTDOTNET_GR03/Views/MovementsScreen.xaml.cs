using CLIMOV_EurekaBank_RESTDOTNET_GR03.Services;

namespace CLIMOV_EurekaBank_RESTDOTNET_GR03.Views
{
    public partial class MovementsScreen : ContentPage
    {
        private readonly EurekaBankClient _client;
        private readonly string _account;
        private readonly CollectionView _movementsList;
        private readonly ActivityIndicator _loading;
        private readonly Label _messageLabel;

        public MovementsScreen(EurekaBankClient client, string account)
        {
            _client = client;
            _account = account;
            BackgroundColor = Color.FromArgb("#F5F5F5");
            NavigationPage.SetHasNavigationBar(this, false);

            _movementsList = new CollectionView { SelectionMode = SelectionMode.None, Margin = new Thickness(0, 8) };
            _movementsList.ItemTemplate = new DataTemplate(() =>
            {
                var tipo = new Label { FontSize = 16, FontAttributes = FontAttributes.Bold };
                tipo.SetBinding(Label.TextProperty, nameof(MovementSummary.TipoImporte));
                tipo.SetBinding(Label.TextColorProperty, nameof(MovementSummary.Color));
                var fecha = new Label { FontSize = 12, TextColor = Color.FromArgb("#777777") };
                fecha.SetBinding(Label.TextProperty, nameof(MovementSummary.Fecha));
                var referencia = new Label { FontSize = 13, TextColor = Color.FromArgb("#4D4D4D"), HorizontalTextAlignment = TextAlignment.End };
                referencia.SetBinding(Label.TextProperty, nameof(MovementSummary.Referencia));
                var saldo = new Label { FontSize = 13, TextColor = Color.FromArgb("#4D4D4D"), HorizontalTextAlignment = TextAlignment.End };
                saldo.SetBinding(Label.TextProperty, nameof(MovementSummary.SaldoTexto));

                var grid = new Grid
                {
                    Padding = new Thickness(18, 12),
                    RowDefinitions = { new RowDefinition(GridLength.Auto), new RowDefinition(GridLength.Auto) },
                    ColumnDefinitions = { new ColumnDefinition(GridLength.Star), new ColumnDefinition(GridLength.Auto) }
                };
                grid.Add(tipo, 0, 0);
                grid.Add(fecha, 0, 1);
                grid.Add(new VerticalStackLayout { Spacing = 4, HorizontalOptions = LayoutOptions.End, Children = { referencia, saldo } }, 1, 0);

                return new VerticalStackLayout { Children = { grid, new BoxView { HeightRequest = 1, Color = Color.FromArgb("#E0E0E0") } } };
            });

            _loading = new ActivityIndicator { Color = Color.FromArgb("#7C3AED"), IsRunning = true };
            _messageLabel = new Label { HorizontalTextAlignment = TextAlignment.Center, TextColor = Color.FromArgb("#D32F2F"), Margin = 20 };
            var newMovement = new Button
            {
                Text = "+",
                WidthRequest = 58,
                HeightRequest = 58,
                CornerRadius = 29,
                BackgroundColor = Color.FromArgb("#7C3AED"),
                TextColor = Colors.White,
                FontSize = 30,
                HorizontalOptions = LayoutOptions.End,
                VerticalOptions = LayoutOptions.End,
                Margin = new Thickness(0, 0, 20, 20)
            };
            newMovement.Clicked += OnNewMovementClicked;

            Content = new Grid
            {
                RowDefinitions = { new RowDefinition(64), new RowDefinition(GridLength.Star) },
                Children =
                {
                    Header(),
                    new Grid { Children = { _movementsList, _loading, _messageLabel, newMovement } }.Row(1)
                }
            };
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await LoadMovementsAsync();
        }

        private async Task LoadMovementsAsync()
        {
            _loading.IsVisible = true;
            _loading.IsRunning = true;
            _messageLabel.Text = string.Empty;
            try
            {
                var movements = await _client.GetMovementsAsync(_account);
                _movementsList.ItemsSource = movements;
                if (movements.Count == 0)
                {
                    _messageLabel.Text = "No hay movimientos.";
                }
            }
            catch (Exception ex)
            {
                _movementsList.ItemsSource = null;
                _messageLabel.Text = $"No se pudieron cargar los movimientos: {ex.Message}";
            }
            finally
            {
                _loading.IsRunning = false;
                _loading.IsVisible = false;
            }
        }

        private async void OnBackClicked(object? sender, EventArgs e)
        {
            await Navigation.PopAsync();
        }

        private Grid Header()
        {
            var header = new Grid
            {
                BackgroundColor = Color.FromArgb("#7C3AED"),
                Padding = new Thickness(8, 0),
                ColumnDefinitions = { new ColumnDefinition(GridLength.Auto), new ColumnDefinition(GridLength.Star) }
            };
            var back = new Button { Text = "‹", BackgroundColor = Colors.Transparent, TextColor = Colors.White, FontSize = 30, WidthRequest = 48 };
            back.Clicked += async (_, _) => await Navigation.PopAsync();
            header.Add(back, 0);
            header.Add(new HorizontalStackLayout
            {
                Spacing = 8,
                VerticalOptions = LayoutOptions.Center,
                HorizontalOptions = LayoutOptions.Center,
                Children =
                {
                    new Image { Source = "sulli_general.png", WidthRequest = 32, HeightRequest = 32 },
                    new Label { Text = $"Movimientos - {_account}", TextColor = Colors.White, FontSize = 18, FontAttributes = FontAttributes.Bold, VerticalTextAlignment = TextAlignment.Center }
                }
            }, 1);
            return header;
        }

        private async void OnNewMovementClicked(object? sender, EventArgs e)
        {
            await Navigation.PushAsync(new NewMovementScreen(_client, _account));
        }
    }
}
