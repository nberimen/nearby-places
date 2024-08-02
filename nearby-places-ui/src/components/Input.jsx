const Input = (props) => {
    const { label, type, placeholder, value, onChange } = props;
    return (
        <div class="input-group">
            <label className="input-group-text" >{label}</label>
            <input
                type={type}
                className="form-control"
                value={value}
                onChange={onChange} />
        </div>
    )
}

export default Input